package com.zenika.ylegat.workshop.infrastructure;

import static java.sql.DriverManager.getConnection;
import static com.github.ylegat.uncheck.Uncheck.uncheck;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.postgresql.PGProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.zenika.ylegat.workshop.domain.common.ConflictingEventException;
import com.zenika.ylegat.workshop.domain.common.Event;
import com.zenika.ylegat.workshop.domain.common.EventBus;
import com.zenika.ylegat.workshop.domain.common.EventStore;

/**
 * with docker-compose:
 * docker-compose up
 * docker-compose exec postgres bash
 *
 * with docker:
 * docker run -i -t -p 5432:5432 ylegat/workshop-event-sourcing:latest
 * docker exec -it 72022a327f4e bash
 *
 * psql -U postgres
 * \connect workshop
 *
 */
public class PostgresEventStore extends EventStore {

    private static class InstantTypeConverter implements JsonSerializer<Instant>, JsonDeserializer<Instant> {
        @Override
        public JsonElement serialize(Instant src, Type srcType, JsonSerializationContext context) {
            return new JsonPrimitive(src.toEpochMilli());
        }

        @Override
        public Instant deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            return Instant.ofEpochMilli(json.getAsLong());
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(PostgresEventStore.class);

    private final Connection connection;

    private final Gson gson;

    public PostgresEventStore(EventBus eventBus) {
        super(eventBus);
        String url = "jdbc:postgresql://localhost:5432/workshop";
        Properties props = new Properties();
        PGProperty.USER.set(props, "postgres");
        PGProperty.PASSWORD.set(props, "");
        connection = uncheck(() -> getConnection(url, props));
        gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantTypeConverter())
                                .create();
    }

    @Override
    public List<Event> load(String aggregateId, int fromAggregateVersion) {
        return uncheck(() -> {
            PreparedStatement statement = connection.prepareStatement("SELECT event_class, data FROM events WHERE events.aggregate_id = ? and events.aggregate_version >= ?");
            statement.setString(1, aggregateId);
            statement.setInt(2, fromAggregateVersion);
            try (ResultSet result = statement.executeQuery()) {
                return deserializeEvents(result);
            }
        });
    }

    @Override
    public void save(int aggregateVersion, List<Event> events) {
        uncheck(() -> {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO events(aggregate_id, aggregate_version, event_class, data) VALUES(?, ?, ?, to_json(?::json))");
            int nextAggregateVersion = aggregateVersion + 1;
            for (Event event : events) {
                statement.setString(1, event.aggregateId);
                statement.setInt(2, nextAggregateVersion++);
                statement.setString(3, event.getClass().getName());
                statement.setObject(4, gson.toJson(event));
                statement.addBatch();
            }

            try {
                int eventsInserted = statement.executeUpdate();
                logger.info("{} events inserted", eventsInserted);

                dispatchToEventBus(events);

            } catch (SQLException e) {
                logger.warn("conflict while committing events {}", events);
                throw new ConflictingEventException();
            }
        });
    }

    @Override
    public void clear() {
        uncheck(() -> connection.prepareStatement("DELETE FROM events").executeUpdate());
    }

    private List<Event> deserializeEvents(ResultSet result) throws SQLException, ClassNotFoundException {
        List<Event> events = new ArrayList<>();
        while (uncheck(result::next)) {
            String eventClass = result.getString(1);
            String data = result.getString(2);
            Event event = gson.fromJson(data, Class.forName(eventClass).asSubclass(Event.class));
            events.add(event);
        }

        return events;
    }
}

package com.zenika.ylegat.workshop.domain.common;

import static java.util.Arrays.asList;
import java.util.List;

public abstract class EventStore {

    private final EventBus eventBus;

    public EventStore(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void save(int aggregateVersion, Event... events) {
        save(aggregateVersion, asList(events));
    }

    public abstract void save(int aggregateVersion, List<Event> events);

    public abstract List<Event> load(String bankAccountId, int fromAggregateVersion);

    public abstract void clear();

    public List<Event> load(String bankAccountId) {
        return load(bankAccountId, 1);
    }

    protected void dispatchToEventBus(List<Event> events) {
        eventBus.push(events);
    }
}

package com.zenika.ylegat.workshop.infrastructure;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static com.zenika.ylegat.workshop.domain.common.EventBus.noEventBus;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import com.zenika.ylegat.workshop.domain.account.BankAccountRegistered;
import com.zenika.ylegat.workshop.domain.common.ConflictingEventException;
import com.zenika.ylegat.workshop.domain.common.Event;
import com.zenika.ylegat.workshop.domain.common.EventStore;

public class PostgresEventStoreTest {

    private final EventStore eventStore = new PostgresEventStore(noEventBus());
    
    @Before
    public void before() {
        eventStore.clear();
    }

    @Test
    public void should_save_unit_of_work_when_no_conflict_with_version() {
        // Given
        BankAccountRegistered event = new BankAccountRegistered("bankAccountId");

        // When
        Throwable conflictingEventException = catchThrowable(() -> eventStore.save(0, event));

        // Then
        assertThat(conflictingEventException).isNull();
    }

    @Test
    public void should_not_save_unit_of_work_when_version_conflict() {
        // Given
        BankAccountRegistered event = new BankAccountRegistered("bankAccountId");
        eventStore.save(0, event);

        // When
        Throwable conflictingEventException = catchThrowable(() -> eventStore.save(0, singletonList(event)));

        // Then
        assertThat(conflictingEventException).isInstanceOf(ConflictingEventException.class);
    }

    @Test
    public void should_return_empty_list_when_loading_no_event() {
        // When
        List<Event> events = eventStore.load("unknownAggregateId");

        // Then
        assertThat(events).isEmpty();
    }

    @Test
    public void should_load_saved_event() {
        // Given
        BankAccountRegistered event = new BankAccountRegistered("bankAccountId");
        eventStore.save(0, event);

        // When
        List<Event> events = eventStore.load("bankAccountId");

        // Then
        assertThat(events).usingRecursiveFieldByFieldElementComparator().containsExactly(event);
    }

    @Test
    public void should_not_load_event_that_was_not_saved() {
        // Given
        BankAccountRegistered event = new BankAccountRegistered("bankAccountId");
        eventStore.save(0, event);
        catchThrowable(() -> eventStore.save(0, singletonList(event)));

        // When
        List<Event> events = eventStore.load("bankAccountId");

        // Then
        assertThat(events).usingRecursiveFieldByFieldElementComparator().containsExactly(event);
    }

}

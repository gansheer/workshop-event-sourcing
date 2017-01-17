package com.zenika.ylegat.workshop.domain.account;

import static java.lang.Math.abs;
import static java.time.Duration.between;
import static org.assertj.core.api.Assertions.assertThat;
import java.time.Instant;
import java.util.List;
import org.assertj.core.api.ListAssert;
import org.junit.After;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zenika.ylegat.workshop.domain.common.Event;
import com.zenika.ylegat.workshop.domain.common.EventBus;
import com.zenika.ylegat.workshop.domain.common.EventStore;
import com.zenika.ylegat.workshop.infrastructure.GuavaEventBus;
import com.zenika.ylegat.workshop.infrastructure.PostgresEventStore;

public abstract class AbstractBankAccountTesting {

    private static final Logger logger = LoggerFactory.getLogger(AbstractBankAccountTesting.class);

    protected static final EventBus eventBus;

    protected static final EventStore eventStore;

    static {
        eventBus = new GuavaEventBus();
        eventStore = new PostgresEventStore(eventBus);
    }

    @After
    public void after() {
        eventStore.clear();
        eventBus.clear();
    }

    protected ListAssert<Event> assertThatEvents(String bankAccountId) {
        List<Event> assertEvents = eventStore.load(bankAccountId);
        logger.debug("assert on events: {}", assertEvents);

        return assertThat(assertEvents)
                .usingComparatorForElementFieldsWithType(this::softInstantComparator, Instant.class)
                .usingRecursiveFieldByFieldElementComparator();
    }

    private int softInstantComparator(Instant i1, Instant i2) {
        int diff = (int) between(i1, i2).getSeconds();
        return abs(diff) <= 1 ? 0 : diff;
    }

}

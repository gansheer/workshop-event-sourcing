package com.zenika.ylegat.workshop.domain.account;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.annotations.VisibleForTesting;
import com.zenika.ylegat.workshop.domain.common.DecisionFunction;
import com.zenika.ylegat.workshop.domain.common.Event;
import com.zenika.ylegat.workshop.domain.common.EventListener;
import com.zenika.ylegat.workshop.domain.common.EventStore;
import com.zenika.ylegat.workshop.domain.common.EvolutionFunction;

public class BankAccount {

    public static Optional<BankAccount> loadBankAccount(String bankAccountId, EventStore eventStore) {
        List<Event> events = eventStore.load(bankAccountId);
        if (events.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new BankAccount(bankAccountId, eventStore, events));
    }

    private static final Logger logger = LoggerFactory.getLogger(BankAccount.class);

    private final InnerEventListener eventProcessor = new InnerEventListener();

    private final EventStore eventStore;

    private String id;

    private int creditBalance;

    private int version;

    private final Map<String, TransferInitialized> pendingTransfers;

    public BankAccount(EventStore eventStore) {
        this(null, eventStore, emptyList());
    }

    private BankAccount(String id, EventStore eventStore, List<Event> events) {
        this.id = id;
        this.eventStore = eventStore;
        this.creditBalance = 0;
        this.version = 0;
        this.pendingTransfers = new HashMap<>();
        events.forEach(eventProcessor::on);
    }

    @VisibleForTesting
    BankAccount(String id,
                EventStore eventStore,
                int creditBalance,
                int aggregateVersion) {
        this(id, eventStore, creditBalance, aggregateVersion, emptyMap());
    }

    @VisibleForTesting
    BankAccount(String id,
                EventStore eventStore,
                int creditBalance,
                int aggregateVersion,
                Map<String, TransferInitialized> pendingTransfers) {
        this.id = id;
        this.eventStore = eventStore;
        this.creditBalance = creditBalance;
        this.version = aggregateVersion;
        this.pendingTransfers = pendingTransfers;
    }

    @DecisionFunction
    public void registerBankAccount(String bankAccountId) {
        /**
         * 1. instantiate a BankAccountRegisteredEvent event
         * 2. save the event in the event store (eventStore.save(...))
         * 3. load the new event from the event store (eventStore.load(id, version + 1))
         * 4. apply the loaded event (eventProcessor.on(event))
         */
    }

    @DecisionFunction
    public void provisionCredit(int creditToProvision) {
        /**
         * 1. instantiate a CreditProvisioned event
         * 2. save the event in the event store (eventStore.save(...))
         * 3. load the new event from the event store (eventStore.load(id, version + 1))
         * 4. apply the loaded event (eventProcessor.on(event))
         */
    }

    @DecisionFunction
    public void withdrawCredit(int creditToWithdraw) {
        /**
         * 1. throw an InvalidCommandException if the balance is lower then the credit amount to withdraw
         * 2. instantiate a CreditWithdrawn event
         * 3. save the event in the event store (eventStore.save(...))
         * 4. load the new event from the event store (eventStore.load(id, version + 1))
         * 5. apply the loaded event (eventProcessor.on(event))
         */
    }

    @DecisionFunction
    public String initializeTransfer(String bankAccountDestinationId, int creditToTransfer) {
        /**
         * 1. throw an InvalidCommandException if the bank destination id is the same that this id
         * 2. throw an InvalidCommandException if the balance is lower then the credit amount to transfer
         * 3. instantiate a TransferInitialized event
         * 4. save the event in the event store (eventStore.save(...))
         * 5. load the new event from the event store (eventStore.load(id, version + 1))
         * 6. apply the loaded event (eventProcessor.on(event))
         */

        return null;
    }

    @DecisionFunction
    public void receiveTransfer(String bankAccountOriginId, String transferId, int creditTransferred) {
        /**
         * 1. instantiate a TransferReceived event
         * 2. save the event in the event store (eventStore.save(...))
         * 3. load the new event from the event store (eventStore.load(id, version + 1))
         * 4. apply the loaded event (eventProcessor.on(event))
         */
    }

    @DecisionFunction
    public void finalizeTransfer(String transferId) {
        /**
         * 1. throw an InvalidCommandException if the transfer id is absent from the pending transfer map
         * 2. instantiate a TransferFinalized event
         * 3. save the event in the event store (eventStore.save(...))
         * 4. load the new event from the event store (eventStore.load(id, version + 1))
         * 5. apply the loaded event (eventProcessor.on(event))
         */
    }

    @DecisionFunction
    public void cancelTransfer(String transferId) {
        /**
         * 1. throw an InvalidCommandException if the transfer id is absent from the pending transfer map
         * 2. instantiate a TransferCanceled event
         * 3. save the event in the event store (eventStore.save(...))
         * 4. load the new event from the event store (eventStore.load(id, version + 1))
         * 5. apply the loaded event (eventProcessor.on(event))
         */
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(creditBalance, that.creditBalance) &&
                Objects.equals(version, that.version) &&
                Objects.equals(pendingTransfers, that.pendingTransfers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "id=" + id +
                ", creditBalance=" + creditBalance +
                ", version=" + version +
                ", pendingTransfers=" + pendingTransfers +
                '}';
    }

    private class InnerEventListener implements EventListener {

        @Override
        public void on(Event event) {
            EventListener.super.on(event);
            version++;
        }

        @Override
        @EvolutionFunction
        public void on(BankAccountRegistered bankAccountRegistered) {
            /**
             * 1. affect the event's aggregate id to this id
             */
        }

        @Override
        @EvolutionFunction
        public void on(CreditProvisioned creditProvisioned) {
            /**
             * 1. affect the event's new credit balance to this credit balance
             */
        }

        @Override
        @EvolutionFunction
        public void on(CreditWithdrawn creditWithdrawn) {
            /**
             * 1. affect the event's new credit balance to this credit balance
             */
        }

        @Override
        @EvolutionFunction
        public void on(TransferInitialized transferInitialized) {
            /**
             * 1. affect the event's new credit balance to this credit balance
             * 2. add the event to the pending transfers map
             */
        }

        @Override
        @EvolutionFunction
        public void on(TransferReceived transferReceived) {
            /**
             * 1. affect the event's new credit balance to this credit balance
             */
        }

        @Override
        @EvolutionFunction
        public void on(TransferFinalized transferFinalized) {
            /**
             * 1. remove the event from the pending transfers map
             */
        }

        @Override
        @EvolutionFunction
        public void on(TransferCanceled transferCanceled) {
            /**
             * 1. affect the event's new credit balance to this credit balance
             * 2. remove the event from the pending transfers map
             */
        }
    }

}

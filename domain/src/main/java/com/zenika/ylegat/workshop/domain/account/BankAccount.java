package com.zenika.ylegat.workshop.domain.account;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.UUID.randomUUID;
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
import com.zenika.ylegat.workshop.domain.common.InvalidCommandException;

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
        eventStore.save(version, new BankAccountRegistered(bankAccountId));
        eventStore.load(bankAccountId).forEach(eventProcessor::on);
    }

    @DecisionFunction
    public void provisionCredit(int creditToProvision) {
        eventStore.save(version, new CreditProvisioned(id, creditToProvision, creditBalance + creditToProvision));

        eventStore.load(id, version + 1)
                  .forEach(eventProcessor::on);
    }

    @DecisionFunction
    public void withdrawCredit(int creditToWithdraw) {
        int newCreditBalance = creditBalance - creditToWithdraw;
        if (newCreditBalance < 0) {
            logger.info("not enough credit ({}) to withdraw {}", creditBalance, creditToWithdraw);
            throw new InvalidCommandException();
        }

        eventStore.save(version, new CreditWithdrawn(id, creditToWithdraw, newCreditBalance));
        eventStore.load(id, version + 1)
                  .forEach(eventProcessor::on);
    }

    @DecisionFunction
    public String initializeTransfer(String bankAccountDestinationId, int creditToTransfer) {
        if (bankAccountDestinationId.equals(id)) {
            logger.info("cannot transfer {} credit to same account {}", creditToTransfer, bankAccountDestinationId);
            throw new InvalidCommandException();
        }

        int newCreditBalance = creditBalance - creditToTransfer;
        if (newCreditBalance < 0) {
            logger.info("not enough credit ({}) to transfer {}", creditBalance, creditToTransfer);
            throw new InvalidCommandException();
        }

        String transferId = randomUUID().toString();
        eventStore.save(version, new TransferInitialized(id,
                                                         transferId,
                                                         bankAccountDestinationId,
                                                         creditToTransfer,
                                                         newCreditBalance));
        eventStore.load(id, version + 1)
                  .forEach(eventProcessor::on);

        return transferId;
    }

    @DecisionFunction
    public void receiveTransfer(String bankAccountOriginId, String transferId, int creditTransferred) {
        eventStore.save(version, new TransferReceived(id,
                                                      transferId,
                                                      bankAccountOriginId,
                                                      creditTransferred,
                                                      creditBalance + creditTransferred));

        eventStore.load(id, version + 1)
                  .forEach(eventProcessor::on);
    }

    @DecisionFunction
    public void finalizeTransfer(String transferId) {
        TransferInitialized transferInitialized = pendingTransfers.get(transferId);
        if (transferInitialized == null) {
            logger.info("transfer designed by id {} has not been initialized or was already finalized", transferId);
            throw new InvalidCommandException();
        }

        eventStore.save(version, new TransferFinalized(id,
                                                       transferId,
                                                       transferInitialized.bankAccountIdDestination));

        eventStore.load(id, version + 1)
                  .forEach(eventProcessor::on);
    }

    @DecisionFunction
    public void cancelTransfer(String transferId) {
        if (!pendingTransfers.containsKey(transferId)) {
            logger.info("transfer designed by id {} has not been initialized or was already finalized", transferId);
            throw new InvalidCommandException();
        }

        TransferInitialized transferInitialized = pendingTransfers.get(transferId);
        eventStore.save(version, new TransferCanceled(id,
                                                      transferId,
                                                      transferInitialized.bankAccountIdDestination,
                                                      transferInitialized.creditTransferred,
                                                      creditBalance + transferInitialized.creditTransferred));

        eventStore.load(id, version + 1)
                  .forEach(eventProcessor::on);
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
            id = bankAccountRegistered.aggregateId;
        }

        @Override
        @EvolutionFunction
        public void on(CreditProvisioned creditProvisioned) {
            creditBalance = creditProvisioned.newCreditBalance;
        }

        @Override
        @EvolutionFunction
        public void on(CreditWithdrawn creditWithdrawn) {
            creditBalance = creditWithdrawn.newCreditBalance;
        }

        @Override
        @EvolutionFunction
        public void on(TransferInitialized transferInitialized) {
            creditBalance = transferInitialized.newCreditBalance;
            pendingTransfers.put(transferInitialized.transferId, transferInitialized);
        }

        @Override
        @EvolutionFunction
        public void on(TransferReceived transferReceived) {
            creditBalance = transferReceived.newCreditBalance;
        }

        @Override
        @EvolutionFunction
        public void on(TransferFinalized transferFinalized) {
            pendingTransfers.remove(transferFinalized.transferId);
        }

        @Override
        @EvolutionFunction
        public void on(TransferCanceled transferCanceled) {
            pendingTransfers.remove(transferCanceled.transferId);
            creditBalance = transferCanceled.newCreditBalance;
        }
    }

}

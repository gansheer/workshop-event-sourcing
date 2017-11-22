package com.zenika.ylegat.workshop.domain.account;

import static com.zenika.ylegat.workshop.domain.account.BankAccount.loadBankAccount;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zenika.ylegat.workshop.domain.common.EventStore;
import com.zenika.ylegat.workshop.domain.common.InvalidCommandException;
import com.zenika.ylegat.workshop.domain.common.ProcessManager;

public class TransferProcessManager implements ProcessManager {

    private static final Logger logger = LoggerFactory.getLogger(TransferProcessManager.class);

    private final EventStore eventStore;

    public TransferProcessManager(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public void on(BankAccountRegistered bankAccountRegistered) {

    }

    @Override
    public void on(CreditProvisioned creditProvisioned) {

    }

    @Override
    public void on(CreditWithdrawn creditWithdrawn) {

    }

    @Override
    public void on(TransferInitialized transferInitialized) {
        Optional<BankAccount> bankAccountDestination = loadBankAccount(transferInitialized.bankAccountIdDestination,
                                                                       eventStore);

        if (bankAccountDestination.isPresent()) {
            bankAccountDestination.get().receiveTransfer(transferInitialized.aggregateId,
                                                         transferInitialized.transferId,
                                                         transferInitialized.creditTransferred);
        } else {
            Optional<BankAccount> bankAccountOrigin = loadBankAccount(transferInitialized.aggregateId, eventStore);
            if (bankAccountOrigin.isPresent()) {
                try {
                    bankAccountOrigin.get().cancelTransfer(transferInitialized.transferId);
                } catch (InvalidCommandException e) {
                    e.printStackTrace();
                }
            } else {
                logger.warn("bank account '{}' does not exist", transferInitialized.aggregateId);
            }
        }
    }

    @Override
    public void on(TransferReceived transferReceived) {
        loadBankAccount(transferReceived.bankAccountIdOrigin, eventStore)
                   .ifPresent(bankAccount -> {
                       try {
                           bankAccount.finalizeTransfer(transferReceived.transferId);
                       } catch (InvalidCommandException e) {
                           e.printStackTrace();
                       }
                   });
    }

    @Override
    public void on(TransferFinalized transferFinalized) {

    }

    @Override
    public void on(TransferCanceled transferCanceled) {

    }
}

package com.zenika.ylegat.workshop.domain.account;

import org.junit.Test;

public class TransferProcessManagerTest extends AbstractBankAccountTesting {

    @Test
    public void should_finalize_transfer() {
        // Given
        TransferProcessManager transferProcessManager = new TransferProcessManager(eventStore);
        eventBus.register(transferProcessManager);

        BankAccount bankAccountDestination = new BankAccount(eventStore);
        bankAccountDestination.registerBankAccount("bankAccountDestinationId");

        BankAccount bankAccountOrigin = new BankAccount(eventStore);
        bankAccountOrigin.registerBankAccount("bankAccountOriginId");
        bankAccountOrigin.provisionCredit(1);

        // When
        String transferId = bankAccountOrigin.initializeTransfer("bankAccountDestinationId", 1);

        // Then
        assertThatEvents("bankAccountOriginId").containsExactly(new BankAccountRegistered("bankAccountOriginId"),
                                                                new CreditProvisioned("bankAccountOriginId", 1, 1),
                                                                new TransferInitialized("bankAccountOriginId",
                                                                                        transferId,
                                                                                        "bankAccountDestinationId",
                                                                                        1,
                                                                                        0),
                                                                new TransferFinalized("bankAccountOriginId",
                                                                                      transferId,
                                                                                      "bankAccountDestinationId"));

        assertThatEvents("bankAccountDestinationId").containsExactly(new BankAccountRegistered("bankAccountDestinationId"),
                                                                     new TransferReceived("bankAccountDestinationId",
                                                                                          transferId,
                                                                                          "bankAccountOriginId",
                                                                                          1,
                                                                                          1));
    }

    @Test
    public void should_cancel_transfer() {
        // Given
        TransferProcessManager transferProcessManager = new TransferProcessManager(eventStore);
        eventBus.register(transferProcessManager);

        BankAccount bankAccountOrigin = new BankAccount(eventStore);
        bankAccountOrigin.registerBankAccount("bankAccountOriginId");
        bankAccountOrigin.provisionCredit(1);

        // When
        String transferId = bankAccountOrigin.initializeTransfer("bankAccountDestinationId", 1);

        // Then
        assertThatEvents("bankAccountOriginId").containsExactly(new BankAccountRegistered("bankAccountOriginId"),
                                                                new CreditProvisioned("bankAccountOriginId", 1, 1),
                                                                new TransferInitialized("bankAccountOriginId",
                                                                                        transferId,
                                                                                        "bankAccountDestinationId",
                                                                                        1,
                                                                                        0),
                                                                new TransferCanceled("bankAccountOriginId",
                                                                                     transferId,
                                                                                     "bankAccountDestinationId",
                                                                                     1,
                                                                                     1));
    }

}

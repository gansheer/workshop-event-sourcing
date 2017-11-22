package com.zenika.ylegat.workshop.domain.account;

import org.junit.Test;

public class TransferProcessManagerTest extends AbstractBankAccountTesting {

    @Test
    public void should_finalize_transfer() {
        // Given
        /**
         * given:
         * 1. a transfer process manager registered with the event bus (the event bus is accessible from the superclass)
         * 2. a bank account ("origin") registered and provisioned with 1 credit
         * 3. a bank account ("destination") registered
         */

        // When
        /**
         * when a transfer is initialized from "origin" to "destination"
         */

        // Then
        /**
         * Then:
         * 1. "origin" events should contains exactly 1 BankAccountRegistered, 1 CreditProvisioned, 1 TransferInitialized and 1 TransferFinalized
         * 2. "destinations" events should contains exactly 1 BankAccountRegistered and TransferReceived
         */
    }

    @Test
    public void should_cancel_transfer() {
        // Given
        /**
         * given:
         * 1. a transfer process manager registered with the event bus (the event bus is accessible from the superclass)
         * 2. a bank account ("origin") registered and provisioned with 1 credit
         */

        // When
        /**
         * when a transfer is initialized from "origin" to a non registered bank account id
         */

        // Then
        /**
         * Then:
         * 1. "origin" events should contains exactly 1 BankAccountRegistered, 1 CreditProvisioned, 1 TransferInitialized and 1 TransferCanceled
         */
    }

}

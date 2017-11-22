package com.zenika.ylegat.workshop.domain.account;

import org.junit.Test;

public class BankAccount_transferInitializedTest extends AbstractBankAccountTesting {

    @Test
    public void should_fail_initializing_transfer_when_destination_is_same_than_initializer() {
        // Given
        /**
         * Given a bank account registered with 1 credit
         */

        // When
        /**
         * when a transfer of 1 credit is initialized (BankAccount.initializeTransfer) at the same destination than the origin account
         */

        // Then
        /**
         * then assert that:
         * 1. the command throws an invalidCommandException exception
         * 2. the events associated to the account only contains one BankAccountRegistered event
         */
    }

    @Test
    public void should_fail_initializing_transfer_when_credit_to_transfer_greater_than_available_credit() {
        // Given
        /**
         * Given a bank account registered with 0 credit
         */

        // When
        /**
         * when a transfer of 1 credit is initialized (BankAccount.initializeTransfer) at destination of another bank account
         */

        // Then
        /**
         * then assert that:
         * 1. the command throws an invalidCommandException exception
         * 2. the events associated to the account only contains one BankAccountRegistered event
         */
    }

    @Test
    public void should_initialize_transfer() {
        // Given
        /**
         * Given a bank account registered and provisionoined with 1 credit
         */

        // When
        /**
         * when a transfer of 1 credit is initialized (BankAccount.initializeTransfer) at destination of another bank account
         */

        // Then
        /**
         * then assert that:
         * 1. the transfer id returned by the command is not null
         * 2. the events associated to the account only contains a BankAccountRegistered, followed by a CreditProvisioned followed by a TransferInitialized event
         * 3. the state of the bank account is:
         * * 0 credit
         * * a version of 3
         * * a pending event map containing the transfer initialized event
         */
    }

}

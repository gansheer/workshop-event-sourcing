package com.zenika.ylegat.workshop.domain.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import org.junit.Test;
import com.zenika.ylegat.workshop.domain.common.ConflictingEventException;

public class BankAccount_registerBankAccountTest extends AbstractBankAccountTesting {

    @Test
    public void should_register_bank_account_with_success() {
        // When
        /**
         * when bank account is registered (instantiate a bank account and call BankAccount.registerBankAccount)
         */

        // Then
        /**
         * 1. assert that the events associated to the bank account contains exactly one BankAccountRegistered event (use assertThatEvents method defined in the superclass)
         * 2. assert on the state of the bank account (you can use Assertion.assertThat(actualBankAccount).isEqualTo(expectedBankAccount)) :
         * * it's id should be identical to the one created
         * * its credit should be equal to 0
         * * its version should be 1 (one event has been applied on the bank account)
         */
    }

    @Test
    public void should_fail_registering_bank_account_with_already_used_id() {
        // Given
        /**
         * Given a bank account registered (instantiate a bank account and call BankAccount.registerBankAccount)
         */

        // When
        /**
         * When a bank account with the same id is registered (use Assertions.catchThrowable(() -> call_registerBankAccount_here() to catch the exception)
         */

        // Then
        /**
         * 1. assert that the command thrown a ConflictingEventException exception
         * 2. assert that the events associated to the bank account contains exactly one BankAccountRegistered event
         */
    }

}

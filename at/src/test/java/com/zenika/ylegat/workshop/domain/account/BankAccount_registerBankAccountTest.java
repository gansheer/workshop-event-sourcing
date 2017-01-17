package com.zenika.ylegat.workshop.domain.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import org.junit.Test;
import com.zenika.ylegat.workshop.domain.common.ConflictingEventException;

public class BankAccount_registerBankAccountTest extends AbstractBankAccountTesting {

    @Test
    public void should_register_bank_account_with_success() {
        // When
        BankAccount bankAccount = new BankAccount(eventStore);
        bankAccount.registerBankAccount("bankAccountId");

        // Then
        assertThatEvents("bankAccountId").containsExactly(new BankAccountRegistered("bankAccountId"));

        assertThat(bankAccount).isEqualTo(new BankAccount("bankAccountId", eventStore, 0, 1));
    }

    @Test
    public void should_fail_registering_bank_account_with_already_used_id() {
        // Given
        BankAccount bankAccount = new BankAccount(eventStore);
        bankAccount.registerBankAccount("bankAccountId");

        // When
        BankAccount bankAccountConflicting = new BankAccount(eventStore);
        Throwable conflictingEventException = catchThrowable(() -> bankAccountConflicting.registerBankAccount("bankAccountId"));

        // Then
        assertThat(conflictingEventException).isInstanceOf(ConflictingEventException.class);
        assertThatEvents("bankAccountId").containsExactly(new BankAccountRegistered("bankAccountId"));
    }

}

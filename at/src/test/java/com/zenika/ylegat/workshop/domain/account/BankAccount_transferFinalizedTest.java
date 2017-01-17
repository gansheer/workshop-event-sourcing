package com.zenika.ylegat.workshop.domain.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import org.junit.Test;
import com.zenika.ylegat.workshop.domain.common.InvalidCommandException;

public class BankAccount_transferFinalizedTest extends AbstractBankAccountTesting {

    @Test
    public void should_fail_finalizing_transfer_never_initialized() {
        // Given
        BankAccount bankAccount = new BankAccount(eventStore);
        bankAccount.registerBankAccount("bankAccountId");

        // When
        Throwable invalidCommandException = catchThrowable(() -> bankAccount.finalizeTransfer("transferId"));

        // Then
        assertThat(invalidCommandException).isInstanceOf(InvalidCommandException.class);
        assertThatEvents("bankAccountId").containsExactly(new BankAccountRegistered("bankAccountId"));
    }

    @Test
    public void should_finalize_pending_transaction_with_success() {
        // Given
        BankAccount bankAccountOrigin = new BankAccount(eventStore);
        bankAccountOrigin.registerBankAccount("bankAccountOriginId");
        bankAccountOrigin.provisionCredit(1);

        String transferId = bankAccountOrigin.initializeTransfer("bankAccountDestinationId", 1);

        // When
        bankAccountOrigin.finalizeTransfer(transferId);

        // Then
        assertThatEvents("bankAccountOriginId").containsOnly(new BankAccountRegistered("bankAccountOriginId"),
                                                             new TransferInitialized("bankAccountOriginId", transferId,
                                                                                     "bankAccountDestinationId",
                                                                                     1,
                                                                                     0),
                                                             new TransferFinalized("bankAccountOriginId",
                                                                                   transferId,
                                                                                   "bankAccountDestinationId"));

        assertThat(bankAccountOrigin).isEqualTo(new BankAccount("bankAccountOriginId", eventStore, 0, 4));
    }

    @Test
    public void should_fail_finalizing_already_finalized_transaction() {
        // Given
        BankAccount bankAccountOrigin = new BankAccount(eventStore);
        bankAccountOrigin.registerBankAccount("bankAccountOriginId");
        bankAccountOrigin.provisionCredit(1);

        String transferId = bankAccountOrigin.initializeTransfer("bankAccountDestinationId", 1);

        bankAccountOrigin.finalizeTransfer(transferId);

        // When
        Throwable invalidCommandException = catchThrowable(() -> bankAccountOrigin.finalizeTransfer(transferId));

        // Then
        assertThat(invalidCommandException).isInstanceOf(InvalidCommandException.class);
    }

}

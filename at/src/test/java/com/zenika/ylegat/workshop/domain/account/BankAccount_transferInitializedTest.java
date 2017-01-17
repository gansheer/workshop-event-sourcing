package com.zenika.ylegat.workshop.domain.account;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import org.junit.Test;
import com.zenika.ylegat.workshop.domain.common.InvalidCommandException;

public class BankAccount_transferInitializedTest extends AbstractBankAccountTesting {

    @Test
    public void should_fail_initializing_transfer_when_destination_is_same_than_initializer() {
        // Given
        BankAccount bankAccountOrigin = new BankAccount(eventStore);
        bankAccountOrigin.registerBankAccount("bankAccountOriginId");

        // When
        Throwable invalidCommandException = catchThrowable(() -> bankAccountOrigin.initializeTransfer("bankAccountOriginId",
                                                                                                      1));

        // Then
        assertThat(invalidCommandException).isInstanceOf(InvalidCommandException.class);
        assertThatEvents("bankAccountOriginId").containsExactly(new BankAccountRegistered("bankAccountOriginId"));
    }

    @Test
    public void should_fail_initializing_transfer_when_credit_to_transfer_greater_than_available_credit() {
        // Given
        BankAccount bankAccountOrigin = new BankAccount(eventStore);
        bankAccountOrigin.registerBankAccount("bankAccountOriginId");

        // When
        Throwable invalidCommandException = catchThrowable(() -> bankAccountOrigin.initializeTransfer("bankAccountDestinationId",
                                                                                                      1));

        // Then
        assertThat(invalidCommandException).isInstanceOf(InvalidCommandException.class);
        assertThatEvents("bankAccountOriginId").containsExactly(new BankAccountRegistered("bankAccountOriginId"));
    }

    @Test
    public void should_initialize_transfer() {
        // Given
        BankAccount bankAccountOrigin = new BankAccount(eventStore);
        bankAccountOrigin.registerBankAccount("bankAccountOriginId");
        bankAccountOrigin.provisionCredit(1);

        // When
        String transferId = bankAccountOrigin.initializeTransfer("bankAccountDestinationId", 1);

        // Then
        assertThat(transferId).isNotNull();

        TransferInitialized transferInitialized = new TransferInitialized("bankAccountOriginId",
                                                                          transferId,
                                                                          "bankAccountDestinationId",
                                                                          1,
                                                                          0);

        assertThatEvents("bankAccountOriginId").containsExactly(new BankAccountRegistered("bankAccountOriginId"),
                                                                new CreditProvisioned("bankAccountOriginId", 1, 1),
                                                                transferInitialized);

        assertThat(bankAccountOrigin).isEqualTo(new BankAccount("bankAccountOriginId",
                                                                eventStore,
                                                                0,
                                                                3,
                                                                singletonMap(transferId, transferInitialized)));
    }

}

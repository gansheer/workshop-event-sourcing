package com.zenika.ylegat.workshop.domain.account;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class BankAccount_transferReceivedTest extends AbstractBankAccountTesting {

    @Test
    public void should_receive_transfer() {
        // Given
        BankAccount bankAccountDestination = new BankAccount(eventStore);
        bankAccountDestination.registerBankAccount("bankAccountDestinationId");

        // When
        bankAccountDestination.receiveTransfer("bankAccountOriginId", "transferId", 1);

        // Then
        assertThatEvents("bankAccountDestinationId").containsExactly(new BankAccountRegistered("bankAccountDestinationId"),
                                                                     new TransferReceived("bankAccountDestinationId",
                                                                                          "transferId",
                                                                                          "bankAccountOriginId",
                                                                                          1,
                                                                                          1));

        assertThat(bankAccountDestination).isEqualTo(new BankAccount("bankAccountDestinationId", eventStore, 1, 2));
    }


}

package com.zenika.ylegat.workshop.projection;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import com.zenika.ylegat.workshop.domain.account.BankAccountRegistered;
import com.zenika.ylegat.workshop.domain.account.CreditProvisioned;
import com.zenika.ylegat.workshop.domain.account.CreditWithdrawn;
import com.zenika.ylegat.workshop.domain.account.TransferCanceled;
import com.zenika.ylegat.workshop.domain.account.TransferInitialized;
import com.zenika.ylegat.workshop.domain.account.TransferReceived;
import com.zenika.ylegat.workshop.projection.balance.CreditBalanceProjectionManager;
import com.zenika.ylegat.workshop.projection.balance.CreditBalanceRepository;
import com.zenika.ylegat.workshop.projection.balance.PostgresCreditBalanceRepository;

public class CreditBalanceProjectionManagerTest {

    private final CreditBalanceProjectionManager creditBalanceProjectionManager;

    private final CreditBalanceRepository totalCreditRepository;

    public CreditBalanceProjectionManagerTest() {
        totalCreditRepository = new PostgresCreditBalanceRepository();
        creditBalanceProjectionManager = new CreditBalanceProjectionManager(totalCreditRepository);
    }

    @Before
    public void before() {
        totalCreditRepository.clear();
    }

    @Test
    public void onBankAccountRegistered() {
        // When
        creditBalanceProjectionManager.on(new BankAccountRegistered("bankAccountId"));

        // Then
        assertThat(totalCreditRepository.readCreditBalance("bankAccountId")).contains(0);
    }

    @Test
    public void onCreditProvisioned() {
        // When
        creditBalanceProjectionManager.on(new CreditProvisioned("bankAccountId", 10, 15));

        // Then
        assertThat(totalCreditRepository.readCreditBalance("bankAccountId")).contains(15);
    }

    @Test
    public void onCreditWithdrawn() {
        // When
        creditBalanceProjectionManager.on(new CreditWithdrawn("bankAccountId", 10, 15));

        // Then
        assertThat(totalCreditRepository.readCreditBalance("bankAccountId")).contains(15);
    }

    @Test
    public void onTransferInitialized() {
        // When
        creditBalanceProjectionManager.on(new TransferInitialized("bankAccountId", "transferId",
                                                                  "bankAccountDestination",
                                                                  10,
                                                                  15));

        // Then
        assertThat(totalCreditRepository.readCreditBalance("bankAccountId")).contains(15);
    }

    @Test
    public void onTransferReceived() {
        // When
        creditBalanceProjectionManager.on(new TransferReceived("bankAccountId",
                                                               "transferId",
                                                               "bankAccountDestination",
                                                               10,
                                                               15));

        // Then
        assertThat(totalCreditRepository.readCreditBalance("bankAccountId")).contains(15);
    }

    @Test
    public void onTransferCanceled() {
        // When
        creditBalanceProjectionManager.on(new TransferCanceled("bankAccountId",
                                                               "transferId",
                                                               "bankAccountDestination",
                                                               10,
                                                               15));

        // Then
        assertThat(totalCreditRepository.readCreditBalance("bankAccountId")).contains(15);
    }

}

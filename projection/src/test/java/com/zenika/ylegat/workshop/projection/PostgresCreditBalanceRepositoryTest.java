package com.zenika.ylegat.workshop.projection;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import com.zenika.ylegat.workshop.projection.balance.PostgresCreditBalanceRepository;

public class PostgresCreditBalanceRepositoryTest {

    private final PostgresCreditBalanceRepository totalCreditRepository;

    public PostgresCreditBalanceRepositoryTest() {
        this.totalCreditRepository = new PostgresCreditBalanceRepository();
    }

    @Before
    public void before() {
        totalCreditRepository.clear();
    }

    @Test
    public void should_write_credit() {
        // When
        totalCreditRepository.writeCreditBalance("bankAccountId", 10);

        // Then
        assertThat(totalCreditRepository.readCreditBalance("bankAccountId")).contains(10);
    }

    @Test
    public void should_update_credit() {
        // When
        totalCreditRepository.writeCreditBalance("bankAccountId", 10);

        // When
        totalCreditRepository.writeCreditBalance("bankAccountId", 15);

        // Then
        assertThat(totalCreditRepository.readCreditBalance("bankAccountId")).contains(15);
    }

}

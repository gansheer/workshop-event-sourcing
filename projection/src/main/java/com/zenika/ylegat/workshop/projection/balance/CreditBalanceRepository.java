package com.zenika.ylegat.workshop.projection.balance;

import java.util.Optional;

public interface CreditBalanceRepository {

    void writeCreditBalance(String bankAccountId, int credit);

    Optional<Integer> readCreditBalance(String bankAccountId);

    void clear();
}

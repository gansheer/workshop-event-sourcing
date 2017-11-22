package com.zenika.ylegat.workshop.projection.balance;

import com.zenika.ylegat.workshop.domain.account.BankAccountRegistered;
import com.zenika.ylegat.workshop.domain.account.CreditProvisioned;
import com.zenika.ylegat.workshop.domain.account.CreditWithdrawn;
import com.zenika.ylegat.workshop.domain.account.TransferCanceled;
import com.zenika.ylegat.workshop.domain.account.TransferFinalized;
import com.zenika.ylegat.workshop.domain.account.TransferInitialized;
import com.zenika.ylegat.workshop.domain.account.TransferReceived;

public class CreditBalanceProjectionManager implements ProjectionManager {

    private final CreditBalanceRepository creditBalanceRepository;

    public CreditBalanceProjectionManager(CreditBalanceRepository creditBalanceRepository) {
        this.creditBalanceRepository = creditBalanceRepository;
    }

    @Override
    public void on(BankAccountRegistered bankAccountRegistered) {
        creditBalanceRepository.writeCreditBalance(bankAccountRegistered.aggregateId, 0);
    }

    @Override
    public void on(CreditProvisioned creditProvisioned) {
        creditBalanceRepository.writeCreditBalance(creditProvisioned.aggregateId, creditProvisioned.newCreditBalance);
    }

    @Override
    public void on(CreditWithdrawn creditWithdrawn) {
        creditBalanceRepository.writeCreditBalance(creditWithdrawn.aggregateId, creditWithdrawn.newCreditBalance);
    }

    @Override
    public void on(TransferInitialized transferInitialized) {
        creditBalanceRepository.writeCreditBalance(transferInitialized.aggregateId, transferInitialized.newCreditBalance);
    }

    @Override
    public void on(TransferReceived transferReceived) {
        creditBalanceRepository.writeCreditBalance(transferReceived.aggregateId, transferReceived.newCreditBalance);
    }

    @Override
    public void on(TransferFinalized transferFinalized) {

    }

    @Override
    public void on(TransferCanceled transferCanceled) {
        creditBalanceRepository.writeCreditBalance(transferCanceled.aggregateId, transferCanceled.newCreditBalance);
    }

}

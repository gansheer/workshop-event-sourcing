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
        /**
         * update the balance of read model using creditBalanceRepository
         */
    }

    @Override
    public void on(CreditProvisioned creditProvisioned) {
        /**
         * update the balance of read model using creditBalanceRepository
         */
    }

    @Override
    public void on(CreditWithdrawn creditWithdrawn) {
        /**
         * update the balance of read model using creditBalanceRepository
         */
    }

    @Override
    public void on(TransferInitialized transferInitialized) {
        /**
         * update the balance of read model using creditBalanceRepository
         */
    }

    @Override
    public void on(TransferReceived transferReceived) {
        /**
         * update the balance of read model using creditBalanceRepository
         */
    }

    @Override
    public void on(TransferFinalized transferFinalized) {

    }

    @Override
    public void on(TransferCanceled transferCanceled) {
        /**
         * update the balance of read model using creditBalanceRepository
         */

    }
}

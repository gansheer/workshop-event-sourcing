package com.zenika.ylegat.workshop.domain.common;

import static java.lang.String.format;
import com.zenika.ylegat.workshop.domain.account.BankAccountRegistered;
import com.zenika.ylegat.workshop.domain.account.CreditProvisioned;
import com.zenika.ylegat.workshop.domain.account.CreditWithdrawn;
import com.zenika.ylegat.workshop.domain.account.TransferCanceled;
import com.zenika.ylegat.workshop.domain.account.TransferFinalized;
import com.zenika.ylegat.workshop.domain.account.TransferInitialized;
import com.zenika.ylegat.workshop.domain.account.TransferReceived;

public interface EventListener {

    default void on(Event event) {
        if (event instanceof BankAccountRegistered) {
            on((BankAccountRegistered) event);
        } else if (event instanceof CreditProvisioned) {
            on((CreditProvisioned) event);
        } else if (event instanceof CreditWithdrawn) {
            on((CreditWithdrawn) event);
        } else if (event instanceof TransferInitialized) {
            on((TransferInitialized) event);
        } else if (event instanceof TransferReceived) {
            on((TransferReceived) event);
        } else if (event instanceof TransferFinalized) {
            on((TransferFinalized) event);
        } else if (event instanceof TransferCanceled) {
            on((TransferCanceled) event);
        } else {
            throw new IllegalStateException(format("bank account event '%s' unknown", event.getClass()));
        }
    }

    void on(BankAccountRegistered bankAccountRegistered);

    void on(CreditProvisioned creditProvisioned);

    void on(CreditWithdrawn creditWithdrawn);

    void on(TransferInitialized transferInitialized);

    void on(TransferReceived transferReceived);

    void on(TransferFinalized transferFinalized);

    void on(TransferCanceled transferCanceled);

}

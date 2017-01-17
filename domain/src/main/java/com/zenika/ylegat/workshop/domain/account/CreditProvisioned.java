package com.zenika.ylegat.workshop.domain.account;

import java.util.Objects;
import com.zenika.ylegat.workshop.domain.common.Event;

public class CreditProvisioned extends Event {

    public final int newCreditBalance;
    public final int creditProvisioned;

    public CreditProvisioned(String bankAccountId, int creditProvisioned, int newCreditBalance) {
        super(bankAccountId);
        this.newCreditBalance = newCreditBalance;
        this.creditProvisioned = creditProvisioned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditProvisioned that = (CreditProvisioned) o;
        return Objects.equals(newCreditBalance, that.newCreditBalance) &&
                Objects.equals(creditProvisioned, that.creditProvisioned);
    }

    @Override
    public int hashCode() {
        return Objects.hash(newCreditBalance, creditProvisioned);
    }

    @Override
    public String toString() {
        return "CreditProvisioned{" +
                "newCredit=" + newCreditBalance +
                ", creditProvisioned=" + creditProvisioned +
                "} " + super.toString();
    }
}

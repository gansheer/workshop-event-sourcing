package com.zenika.ylegat.workshop.domain.account;

import java.util.Objects;
import com.zenika.ylegat.workshop.domain.common.Event;

public class TransferInitialized extends Event {

    public final String bankAccountIdDestination;

    public final String transferId;

    public final int newCreditBalance;

    public final int creditTransferred;

    public TransferInitialized(String bankAccountId, String transferId,
                               String bankAccountIdDestination,
                               int creditTransferred,
                               int newCreditBalance) {
        super(bankAccountId);
        this.transferId = transferId;
        this.bankAccountIdDestination = bankAccountIdDestination;
        this.newCreditBalance = newCreditBalance;
        this.creditTransferred = creditTransferred;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferInitialized that = (TransferInitialized) o;
        return Objects.equals(bankAccountIdDestination, that.bankAccountIdDestination) &&
                Objects.equals(transferId, that.transferId) &&
                Objects.equals(newCreditBalance, that.newCreditBalance) &&
                Objects.equals(creditTransferred, that.creditTransferred);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bankAccountIdDestination, transferId, newCreditBalance, creditTransferred);
    }

    @Override
    public String toString() {
        return "TransferInitialized{" +
                "bankAccountIdDestination=" + bankAccountIdDestination +
                ", transferId=" + transferId +
                ", newCredit=" + newCreditBalance +
                ", creditTransferred=" + creditTransferred +
                "} " + super.toString();
    }
}

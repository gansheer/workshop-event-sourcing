package com.zenika.ylegat.workshop.domain.account;

import java.util.Objects;
import com.zenika.ylegat.workshop.domain.common.Event;

public class TransferFinalized extends Event {

    public final String transferId;

    public final String bankAccountIdDestination;

    public TransferFinalized(String aggregateId, String transferId, String bankAccountIdDestination) {
        super(aggregateId);
        this.transferId = transferId;
        this.bankAccountIdDestination = bankAccountIdDestination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferFinalized that = (TransferFinalized) o;
        return Objects.equals(transferId, that.transferId) &&
                Objects.equals(bankAccountIdDestination, that.bankAccountIdDestination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transferId, bankAccountIdDestination);
    }

    @Override
    public String toString() {
        return "TransferFinalized{" +
                "transferId=" + transferId +
                ", bankAccountIdDestination=" + bankAccountIdDestination +
                '}';
    }
}

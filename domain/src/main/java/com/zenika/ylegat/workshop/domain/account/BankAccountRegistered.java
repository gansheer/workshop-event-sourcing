package com.zenika.ylegat.workshop.domain.account;

import com.zenika.ylegat.workshop.domain.common.Event;

public class BankAccountRegistered extends Event {

    public BankAccountRegistered(String bankAccountId) {
        super(bankAccountId);
    }

    @Override
    public String toString() {
        return "BankAccountRegistered{} " + super.toString();
    }
}

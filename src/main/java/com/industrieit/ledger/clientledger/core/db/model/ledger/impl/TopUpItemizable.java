package com.industrieit.ledger.clientledger.core.db.model.ledger.impl;

import com.industrieit.ledger.clientledger.core.db.entity.Account;
import com.industrieit.ledger.clientledger.core.db.entity.JournalEntry;
import com.industrieit.ledger.clientledger.core.db.model.ledger.Itemizable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TopUpItemizable implements Itemizable {
    private final Account topUp;
    private final Account settlement;
    private final BigDecimal amount;
    private final String currency;
    private final String requestId;


    public TopUpItemizable(Account topUp, Account settlement, BigDecimal amount, String currency, String requestId) {
        this.topUp = topUp;
        this.settlement = settlement;
        this.amount = amount;
        this.currency = currency;
        this.requestId = requestId;
    }

    @Override
    public List<JournalEntry> itemize() {
        List<JournalEntry> journalEntries = new ArrayList<>();
        journalEntries.add(new JournalEntry(settlement, currency, amount.negate(), getRequestId()));
        journalEntries.add(new JournalEntry(topUp, currency, amount, getRequestId()));
        return journalEntries;
    }

    @Override
    public String getRequestId() {
        return requestId;
    }
}

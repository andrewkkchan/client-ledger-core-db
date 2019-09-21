package com.industrieit.ledger.clientledger.core.db.entity;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class JournalEntryTest {
    @Test
    public void testGetSet(){
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setAmount(BigDecimal.TEN);
        journalEntry.setAccount(new Account());
        journalEntry.setCreateTime(new Timestamp(10000));
        journalEntry.setCurrency("USD");
        journalEntry.setId("12345");
        journalEntry.setRequestId("1234567");
        Assert.assertEquals(BigDecimal.TEN, journalEntry.getAmount());
        Assert.assertNotNull(journalEntry.getAccount());
        Assert.assertEquals(new Timestamp(10000), journalEntry.getCreateTime());
        Assert.assertEquals("USD", journalEntry.getCurrency());
        Assert.assertEquals("12345", journalEntry.getId());
        Assert.assertEquals("1234567", journalEntry.getRequestId());

    }
}

package com.industrieit.ledger.clientledger.core.db.entity;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Timestamp;

public class TransactionEventTest {
    @Test
    public void testGetSet() {
        TransactionEvent transactionEvent = new TransactionEvent();
        transactionEvent.setType("P2P");
        transactionEvent.setId("12345");
        transactionEvent.setRequest("{}");
        transactionEvent.setCreateTime(new Timestamp(10000));
        Assert.assertEquals("P2P", transactionEvent.getType());
        Assert.assertEquals("12345", transactionEvent.getId());
        Assert.assertEquals("{}", transactionEvent.getRequest());
        Assert.assertEquals(new Timestamp(10000), transactionEvent.getCreateTime());

    }
}

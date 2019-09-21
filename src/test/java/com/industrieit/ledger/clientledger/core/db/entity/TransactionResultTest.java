package com.industrieit.ledger.clientledger.core.db.entity;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Timestamp;

public class TransactionResultTest {
    @Test
    public void testGetSet(){
        TransactionResult transactionResult = new TransactionResult();
        transactionResult.setSuccess(true);
        transactionResult.setResponse("{}");
        transactionResult.setId("12345");
        transactionResult.setRequestId("444");
        transactionResult.setCreateTime(new Timestamp(1999));
        Assert.assertEquals("{}", transactionResult.getResponse());
        Assert.assertEquals("12345", transactionResult.getId());
        Assert.assertEquals("444", transactionResult.getRequestId());
        Assert.assertEquals(new Timestamp(1999), transactionResult.getCreateTime());
        Assert.assertTrue(transactionResult.isSuccess());

    }
}

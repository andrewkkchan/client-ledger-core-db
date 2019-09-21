package com.industrieit.ledger.clientledger.core.db.entity;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Timestamp;

public class AccountTest {
    @Test
    public void testGetSet(){
        Account account = new Account();
        account.setCurrency("USD");
        account.setAccountName("Hello");
        account.setAccountGroup("Settlement");
        account.setId("1234");
        account.setCreateTime(new Timestamp(10000));
        Assert.assertEquals("USD", account.getCurrency());
        Assert.assertEquals("Hello", account.getAccountName());
        Assert.assertEquals("Settlement", account.getAccountGroup());
        Assert.assertEquals("1234", account.getId());
        Assert.assertEquals(new Timestamp(10000), account.getCreateTime());
    }
}

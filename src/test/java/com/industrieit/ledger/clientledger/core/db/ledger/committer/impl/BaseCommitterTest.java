package com.industrieit.ledger.clientledger.core.db.ledger.committer.impl;

import com.google.common.collect.Iterables;
import com.industrieit.ledger.clientledger.core.db.entity.Account;
import com.industrieit.ledger.clientledger.core.db.entity.JournalEntry;
import com.industrieit.ledger.clientledger.core.db.exception.InvalidBusinessRuleException;
import com.industrieit.ledger.clientledger.core.db.repository.AccountRepository;
import com.industrieit.ledger.clientledger.core.db.repository.JournalEntryRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.ArgumentMatchers.nullable;

public class BaseCommitterTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    private JournalEntryRepository journalEntryRepository;
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private BaseCommitter baseCommitter;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCommit_unBalanced() {
        expectedException.expect(InvalidBusinessRuleException.class);
        expectedException.expectMessage("Unbalanced journal entries");
        List<JournalEntry> journalEntries = new ArrayList<>();
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setAmount(BigDecimal.valueOf(10));
        journalEntries.add(journalEntry);
        journalEntries.add(journalEntry);
        Mockito.when(journalEntryRepository.saveAll(anyIterable())).thenReturn(journalEntries);
        Iterable<JournalEntry> commit = baseCommitter.commit(journalEntries, 0L, 0);
    }

    @Test
    public void testCommit_balanced() {
        List<JournalEntry> journalEntries = new ArrayList<>();
        Account account = new Account();
        account.setBalance(BigDecimal.TEN);
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setAmount(BigDecimal.valueOf(10));
        journalEntry.setAccount(account);
        journalEntries.add(journalEntry);
        JournalEntry balancingJournalEntry = new JournalEntry();
        balancingJournalEntry.setAmount(BigDecimal.valueOf(-10));
        balancingJournalEntry.setAccount(account);
        journalEntries.add(balancingJournalEntry);
        Mockito.when(journalEntryRepository.saveAll(anyIterable())).thenReturn(journalEntries);
        Mockito.when(accountRepository.save(nullable(Account.class))).thenReturn(new Account());
        Iterable<JournalEntry> commit = baseCommitter.commit(journalEntries, 0L, 0);
        Assert.assertNotNull(commit);
        Assert.assertEquals(2, Iterables.size(commit));
    }

    @Test
    public void testCommit_null() {
        Iterable<JournalEntry> commit = baseCommitter.commit(null, 0L, 0);
        Assert.assertNotNull(commit);
        Assert.assertEquals(0, Iterables.size(commit));
    }
}

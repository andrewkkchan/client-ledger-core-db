package com.industrieit.ledger.clientledger.core.db.service.impl;

import com.industrieit.ledger.clientledger.core.db.entity.JournalEntry;
import com.industrieit.ledger.clientledger.core.db.ledger.committer.Committer;
import com.industrieit.ledger.clientledger.core.db.ledger.validator.Validator;
import com.industrieit.ledger.clientledger.core.db.model.ledger.impl.P2PItemizable;
import com.industrieit.ledger.clientledger.core.db.model.request.impl.P2PRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.nullable;

public class P2PServiceImplTest {
    @Mock
    private Validator<P2PRequest> p2PTransactionValidator;
    @Mock
    private Committer committer;
    @InjectMocks
    private P2PServiceImpl p2PService;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testJournal() {
        Mockito.when(p2PTransactionValidator.validate(nullable(String.class), nullable(P2PRequest.class)))
                .thenReturn(new P2PItemizable(null, null, null, null, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, "USD", "1234567"));
        List<JournalEntry> journalEntries = new ArrayList<>();
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setAmount(BigDecimal.valueOf(10));
        journalEntries.add(journalEntry);
        journalEntries.add(journalEntry);
        Mockito.when(committer.commit(anyList(), nullable(long.class), nullable(Integer.class))).thenReturn(journalEntries);
        Iterable<JournalEntry> journal = p2PService.journal("123", new P2PRequest("USD", "123456789", "34556903003",
                "67890", "89777",
                BigDecimal.valueOf(100), BigDecimal.valueOf(10), BigDecimal.valueOf(5)), 0L, 0);
        Assert.assertNotNull(journal);
        for (JournalEntry journalEntry1 : journal) {
            Assert.assertEquals(BigDecimal.valueOf(10), journalEntry1.getAmount());
        }
    }
}

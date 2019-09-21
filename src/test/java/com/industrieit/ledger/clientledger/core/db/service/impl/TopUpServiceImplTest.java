package com.industrieit.ledger.clientledger.core.db.service.impl;

import com.industrieit.ledger.clientledger.core.db.entity.JournalEntry;
import com.industrieit.ledger.clientledger.core.db.ledger.committer.Committer;
import com.industrieit.ledger.clientledger.core.db.ledger.validator.Validator;
import com.industrieit.ledger.clientledger.core.db.model.ledger.impl.TopUpItemizable;
import com.industrieit.ledger.clientledger.core.db.model.request.impl.TopUpRequest;
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

public class TopUpServiceImplTest {
    @Mock
    private Validator<TopUpRequest> validator;
    @Mock
    private Committer committer;
    @InjectMocks
    private TopUpServiceImpl topUpService;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testJournal(){
        List<JournalEntry> journalEntries = new ArrayList<>();
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setAmount(BigDecimal.valueOf(10));
        journalEntries.add(journalEntry);
        journalEntries.add(journalEntry);
        Mockito.when(validator.validate(nullable(String.class), nullable(TopUpRequest.class)))
                .thenReturn(new TopUpItemizable(null, null, BigDecimal.TEN, null, "1234567"));
        Mockito.when(committer.commit(anyList(), nullable(long.class), nullable(Integer.class))).thenReturn(journalEntries);
        TopUpRequest topUpRequest = new TopUpRequest();
        Iterable<JournalEntry> journal = topUpService.journal("123", topUpRequest, 0L, 0);
        Assert.assertNotNull(journal);
        for (JournalEntry journalEntry1 : journal) {
            Assert.assertEquals(BigDecimal.valueOf(10), journalEntry1.getAmount());
        }
    }
}

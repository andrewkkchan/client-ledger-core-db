package com.industrieit.ledger.clientledger.core.db.consumer.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.industrieit.ledger.clientledger.core.db.entity.JournalEntry;
import com.industrieit.ledger.clientledger.core.db.entity.TransactionEvent;
import com.industrieit.ledger.clientledger.core.db.exception.InvalidBusinessRuleException;
import com.industrieit.ledger.clientledger.core.db.model.ledger.Type;
import com.industrieit.ledger.clientledger.core.db.model.request.impl.P2PRequest;
import com.industrieit.ledger.clientledger.core.db.service.JournalService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.nullable;

public class P2PProcessorTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private JournalService<P2PRequest> p2PService;
    @Mock
    private ProducerImpl resultProcessor;
    @InjectMocks
    private P2PProcessor p2PProcessor;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcess() throws IOException {
        P2PRequest p2PRequest = new P2PRequest();
        Mockito.when(objectMapper.readValue(nullable(String.class), ArgumentMatchers.eq(P2PRequest.class))).thenReturn(p2PRequest);
        List<JournalEntry> journalEntries = new ArrayList<>();
        Mockito.when(p2PService.journal(nullable(String.class), nullable(P2PRequest.class), nullable(long.class), nullable(Integer.class)))
                .thenReturn(journalEntries);
        TransactionEvent transactionEvent = new TransactionEvent();
        transactionEvent.setId("abcd1234");
        transactionEvent.setRequest("{}");
        transactionEvent.setKafkaPartition(0);
        transactionEvent.setKafkaOffset(0L);
        transactionEvent.setType(Type.P2P.toString());
        p2PProcessor.process(transactionEvent);
        Mockito.verify(resultProcessor).produceSuccess(nullable(String.class), nullable(Object.class), nullable(long.class), nullable(Integer.class));
        Mockito.verify(resultProcessor, Mockito.never()).produceError(nullable(String.class), nullable(InvalidBusinessRuleException.class),  nullable(long.class), nullable(Integer.class));
    }

    @Test
    public void testProcess_cannotRead() throws IOException {
        Mockito.when(objectMapper.readValue(nullable(String.class), ArgumentMatchers.eq(P2PRequest.class))).thenThrow(new IOException());
        List<JournalEntry> journalEntries = new ArrayList<>();
        Mockito.when(p2PService.journal(nullable(String.class), nullable(P2PRequest.class), nullable(long.class), nullable(Integer.class)))
                .thenReturn(journalEntries);
        TransactionEvent transactionEvent = new TransactionEvent();
        transactionEvent.setId("abcd1234");
        transactionEvent.setRequest("{}");
        transactionEvent.setKafkaPartition(0);
        transactionEvent.setKafkaOffset(0L);
        transactionEvent.setType(Type.P2P.toString());
        p2PProcessor.process(transactionEvent);
        Mockito.verify(resultProcessor, Mockito.never()).produceSuccess(nullable(String.class), nullable(Object.class), nullable(long.class), nullable(Integer.class));
        Mockito.verify(resultProcessor).produceError(nullable(String.class), nullable(InvalidBusinessRuleException.class),  nullable(long.class), nullable(Integer.class));
    }

    @Test
    public void testProcess_serviceFail() throws IOException {
        P2PRequest p2PRequest = new P2PRequest();
        Mockito.when(objectMapper.readValue(nullable(String.class), ArgumentMatchers.eq(P2PRequest.class))).thenReturn(p2PRequest);
        Mockito.when(p2PService.journal(nullable(String.class), nullable(P2PRequest.class), nullable(long.class), nullable(Integer.class)))
                .thenThrow(new InvalidBusinessRuleException("service fails"));
        TransactionEvent transactionEvent = new TransactionEvent();
        transactionEvent.setId("abcd1234");
        transactionEvent.setRequest("{}");
        transactionEvent.setKafkaPartition(0);
        transactionEvent.setKafkaOffset(0L);
        transactionEvent.setType(Type.P2P.toString());
        p2PProcessor.process(transactionEvent);
        Mockito.verify(resultProcessor, Mockito.never()).produceSuccess(nullable(String.class), nullable(Object.class), nullable(long.class), nullable(Integer.class));
        Mockito.verify(resultProcessor).produceError(nullable(String.class), nullable(InvalidBusinessRuleException.class),  nullable(long.class), nullable(Integer.class));
    }

    @Test
    public void testGetType() {
        Assert.assertEquals(Type.P2P.toString(), p2PProcessor.getType());
    }
}

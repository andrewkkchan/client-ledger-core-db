package com.industrieit.ledger.clientledger.core.db.consumer.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.industrieit.ledger.clientledger.core.db.consumer.Processor;
import com.industrieit.ledger.clientledger.core.db.consumer.Producer;

import com.industrieit.ledger.clientledger.core.db.entity.JournalEntry;
import com.industrieit.ledger.clientledger.core.db.entity.TransactionEvent;
import com.industrieit.ledger.clientledger.core.db.exception.InvalidBusinessRuleException;
import com.industrieit.ledger.clientledger.core.db.model.ledger.Type;
import com.industrieit.ledger.clientledger.core.db.model.request.impl.P2PRequest;
import com.industrieit.ledger.clientledger.core.db.service.JournalService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Component
public class P2PProcessor implements Processor {
    private final ObjectMapper objectMapper;
    private final JournalService<P2PRequest> p2PService;
    private final Producer producer;


    public P2PProcessor(ObjectMapper objectMapper,
                        JournalService<P2PRequest> p2PService, Producer producer) {
        this.objectMapper = objectMapper;
        this.p2PService = p2PService;
        this.producer = producer;
    }

    public void process(TransactionEvent transactionEvent) {
        String requestId = transactionEvent.getId();

        P2PRequest p2PRequest;
        try {
            p2PRequest = objectMapper.readValue(transactionEvent.getRequest(), P2PRequest.class);
        } catch (IOException e) {
            producer.produceError(requestId, new InvalidBusinessRuleException("Malformed request"),
                    transactionEvent.getKafkaOffset(), transactionEvent.getKafkaPartition());
            return;
        }
        try {
            journalAndProduce(requestId, p2PRequest, transactionEvent.getKafkaOffset(), transactionEvent.getKafkaPartition());
        } catch (InvalidBusinessRuleException e) {
            producer.produceError(requestId, e, transactionEvent.getKafkaOffset(), transactionEvent.getKafkaPartition());
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void journalAndProduce(String requestId, P2PRequest p2PRequest, long kafkaOffset, Integer kafkaPartition) {
        Iterable<JournalEntry> transactionLogs = this.p2PService.journal(requestId, p2PRequest, kafkaOffset, kafkaPartition);
        producer.produceSuccess(requestId, transactionLogs, kafkaOffset, kafkaPartition);
    }

    @Override
    public String getType() {
        return Type.P2P.toString();
    }


}

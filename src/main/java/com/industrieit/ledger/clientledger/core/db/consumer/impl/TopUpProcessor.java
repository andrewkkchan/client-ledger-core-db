package com.industrieit.ledger.clientledger.core.db.consumer.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.industrieit.ledger.clientledger.core.db.consumer.Producer;
import com.industrieit.ledger.clientledger.core.db.consumer.Processor;
import com.industrieit.ledger.clientledger.core.db.entity.JournalEntry;
import com.industrieit.ledger.clientledger.core.db.entity.TransactionEvent;
import com.industrieit.ledger.clientledger.core.db.exception.InvalidBusinessRuleException;
import com.industrieit.ledger.clientledger.core.db.model.ledger.Type;
import com.industrieit.ledger.clientledger.core.db.model.request.impl.TopUpRequest;
import com.industrieit.ledger.clientledger.core.db.service.JournalService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Component
public class TopUpProcessor implements Processor {
    private final ObjectMapper objectMapper;
    private final Producer producer;
    private final JournalService<TopUpRequest> topUpService;

    public TopUpProcessor(ObjectMapper objectMapper,
                          Producer producer, JournalService<TopUpRequest> topUpService) {
        this.objectMapper = objectMapper;
        this.producer = producer;
        this.topUpService = topUpService;
    }


    @Override
    public void process(TransactionEvent transactionEvent) {
        String requestId = transactionEvent.getId();

        TopUpRequest topUpRequest;
        try {
            topUpRequest = objectMapper.readValue(transactionEvent.getRequest(), TopUpRequest.class);
        } catch (IOException e) {
            producer.produceError(requestId, new InvalidBusinessRuleException("Malformed request"), transactionEvent.getKafkaOffset(), transactionEvent.getKafkaPartition());
            return;
        }

        try {
            journalAndProduce(requestId, topUpRequest, transactionEvent.getKafkaOffset(), transactionEvent.getKafkaPartition());
        } catch (InvalidBusinessRuleException e) {
            producer.produceError(requestId, e, transactionEvent.getKafkaOffset(), transactionEvent.getKafkaPartition());
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void journalAndProduce(String requestId, TopUpRequest topUpRequest, long kafkaOffset, Integer kafkaPartition) {
        Iterable<JournalEntry> transactionLogs = this.topUpService.journal(requestId, topUpRequest, kafkaOffset, kafkaPartition);
        producer.produceSuccess(requestId, transactionLogs, kafkaOffset, kafkaPartition);
    }

    @Override
    public String getType() {
        return Type.TOP_UP.toString();
    }
}

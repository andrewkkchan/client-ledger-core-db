package com.industrieit.ledger.clientledger.core.db.consumer.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.industrieit.ledger.clientledger.core.db.consumer.Producer;
import com.industrieit.ledger.clientledger.core.db.entity.TransactionResult;
import com.industrieit.ledger.clientledger.core.db.exception.InvalidBusinessRuleException;
import com.industrieit.ledger.clientledger.core.db.repository.TransactionResultRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;

@Component
public class ProducerImpl implements Producer {
    private final TransactionResultRepository transactionResultRepository;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, TransactionResult> kafkaTemplate;
    private static final String TOPIC = "Transaction_Result_Maria";


    public ProducerImpl(TransactionResultRepository transactionResultRepository, ObjectMapper objectMapper,
                        KafkaTemplate<String, TransactionResult> kafkaTemplate) {
        this.transactionResultRepository = transactionResultRepository;
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void produceError(String requestId, InvalidBusinessRuleException e, long kafkaOffset, Integer kafkaPartition) {
        TransactionResult transactionResult = prepareResult(requestId, kafkaOffset, kafkaPartition);
        if (e != null) {
            transactionResult.setResponse("{\"message\": \"" + e.getMessage() + "\"}");
        } else {
            transactionResult.setResponse("{\"message\": null}");
        }
        transactionResult.setSuccess(false);
        TransactionResult save = transactionResultRepository.save(transactionResult);
        kafkaTemplate.send(TOPIC, save);

    }
    private TransactionResult prepareResult(String requestId, long kafkaOffset, Integer kafkaPartition) {
        TransactionResult transactionResult = new TransactionResult();
        transactionResult.setRequestId(requestId);
        transactionResult.setKafkaOffset(kafkaOffset);
        transactionResult.setKafkaPartition(kafkaPartition);
        transactionResult.setCreateTime(new Timestamp(new Date().getTime()));
        return transactionResult;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public <T> void produceSuccess(String requestId, T response, long kafkaOffset, Integer kafkaPartition) {
        TransactionResult transactionResult = prepareResult(requestId, kafkaOffset, kafkaPartition);
        try {
            transactionResult.setResponse(objectMapper.writeValueAsString(response));
        } catch (JsonProcessingException e) {
            transactionResult.setResponse("{}");
        }
        transactionResult.setSuccess(true);
        TransactionResult save = transactionResultRepository.save(transactionResult);
        kafkaTemplate.send(TOPIC, save);
    }
}

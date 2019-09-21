package com.industrieit.ledger.clientledger.core.db.consumer.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.industrieit.ledger.clientledger.core.db.consumer.Processor;
import com.industrieit.ledger.clientledger.core.db.consumer.Producer;

import com.industrieit.ledger.clientledger.core.db.entity.Account;
import com.industrieit.ledger.clientledger.core.db.entity.TransactionEvent;
import com.industrieit.ledger.clientledger.core.db.exception.InvalidBusinessRuleException;
import com.industrieit.ledger.clientledger.core.db.model.ledger.Type;
import com.industrieit.ledger.clientledger.core.db.model.request.impl.CreateAccountRequest;
import com.industrieit.ledger.clientledger.core.db.service.AccountService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Component
public class CreateAccountProcessor implements Processor {
    private final ObjectMapper objectMapper;
    private final AccountService accountService;
    private final Producer producer;


    public CreateAccountProcessor(ObjectMapper objectMapper, AccountService accountService, Producer producer) {
        this.objectMapper = objectMapper;
        this.accountService = accountService;
        this.producer = producer;
    }

    public void process(TransactionEvent transactionEvent) {
        String requestId = transactionEvent.getId();

        CreateAccountRequest createAccountRequest;
        try {
            createAccountRequest = objectMapper.readValue(transactionEvent.getRequest(), CreateAccountRequest.class);
        } catch (IOException e) {
            producer.produceError(requestId, new InvalidBusinessRuleException("Malformed request"),
                    transactionEvent.getKafkaOffset(), transactionEvent.getKafkaPartition());
            return;
        }
        try {
            createAccountAndProduce(requestId, createAccountRequest, transactionEvent.getKafkaOffset(), transactionEvent.getKafkaPartition());
        } catch (InvalidBusinessRuleException e) {
            producer.produceError(requestId, e, transactionEvent.getKafkaOffset(), transactionEvent.getKafkaPartition());
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createAccountAndProduce(String requestId, CreateAccountRequest createAccountRequest, long kafkaOffset, Integer kafkaPartition) {
        Account account = this.accountService.createAccount(createAccountRequest);
        producer.produceSuccess(requestId, account, kafkaOffset, kafkaPartition);
    }

    @Override
    public String getType() {
        return Type.CREATE_ACCOUNT.toString();
    }
}

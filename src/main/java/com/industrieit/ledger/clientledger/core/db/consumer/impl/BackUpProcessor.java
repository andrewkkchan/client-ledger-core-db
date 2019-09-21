package com.industrieit.ledger.clientledger.core.db.consumer.impl;

import com.industrieit.ledger.clientledger.core.db.consumer.Processor;
import com.industrieit.ledger.clientledger.core.db.consumer.Producer;
import com.industrieit.ledger.clientledger.core.db.entity.TransactionEvent;
import com.industrieit.ledger.clientledger.core.db.model.ledger.Type;
import org.springframework.stereotype.Component;

@Component
public class BackUpProcessor implements Processor {
    private final Producer producer;

    public BackUpProcessor(Producer producer) {
        this.producer = producer;
    }


    @Override
    public void process(TransactionEvent transactionEvent) {
        producer.produceSuccess(transactionEvent.getId(), null,
                transactionEvent.getKafkaOffset(), transactionEvent.getKafkaPartition());
    }

    @Override
    public String getType() {
        return Type.BACK_UP.toString();
    }
}

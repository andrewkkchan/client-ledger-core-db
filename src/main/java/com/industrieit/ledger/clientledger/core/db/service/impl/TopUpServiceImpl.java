package com.industrieit.ledger.clientledger.core.db.service.impl;

import com.industrieit.ledger.clientledger.core.db.ledger.committer.Committer;
import com.industrieit.ledger.clientledger.core.db.ledger.validator.Validator;
import com.industrieit.ledger.clientledger.core.db.model.ledger.Itemizable;
import com.industrieit.ledger.clientledger.core.db.model.request.impl.TopUpRequest;
import com.industrieit.ledger.clientledger.core.db.entity.JournalEntry;
import com.industrieit.ledger.clientledger.core.db.service.JournalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TopUpServiceImpl implements JournalService<TopUpRequest> {
    private final Validator<TopUpRequest> validator;

    private final Committer committer;

    public TopUpServiceImpl(Validator<TopUpRequest> validator, Committer committer) {
        this.validator = validator;
        this.committer = committer;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public Iterable<JournalEntry> journal(String requestId, TopUpRequest request, Long kafkaOffset, Integer kafkaPartition) {
        Itemizable itemizable = validator.validate(requestId, request);
        return committer.commit(itemizable.itemize(), kafkaOffset, kafkaPartition);
    }
}

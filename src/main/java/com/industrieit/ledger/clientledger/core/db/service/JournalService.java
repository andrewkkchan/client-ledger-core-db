package com.industrieit.ledger.clientledger.core.db.service;

import com.industrieit.ledger.clientledger.core.db.entity.TransactionEvent;
import com.industrieit.ledger.clientledger.core.db.model.request.EventRequest;
import com.industrieit.ledger.clientledger.core.db.entity.JournalEntry;

/**
 * Serializable isolated transactional service to mutate {@link JournalEntry}
 * @param <T> the type of {@link EventRequest} seeking to mutate {@link JournalEntry}
 */
public interface JournalService <T extends EventRequest> {
    /**
     * Journal {@link EventRequest} as a group of atomic {@link JournalEntry}
     * @param requestId ID which uniquely identifies the originating {@link TransactionEvent}
     * @param request {@link EventRequest} seeking to mutate {@link JournalEntry}
     * @param kafkaOffset
     * @param kafkaPartition
     * @return the atomic group of {@link JournalEntry} committed to the ledger
     */
    Iterable<JournalEntry> journal(String requestId, T request, Long kafkaOffset, Integer kafkaPartition);
}

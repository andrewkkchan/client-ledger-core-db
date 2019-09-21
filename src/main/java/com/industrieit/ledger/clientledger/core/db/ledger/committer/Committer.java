package com.industrieit.ledger.clientledger.core.db.ledger.committer;

import com.industrieit.ledger.clientledger.core.db.entity.TransactionEvent;
import com.industrieit.ledger.clientledger.core.db.exception.InvalidBusinessRuleException;
import com.industrieit.ledger.clientledger.core.db.entity.JournalEntry;

/**
 * Committer to commit atomically a group of {@link JournalEntry} which represents one {@link TransactionEvent}
 */
public interface Committer {
    /**
     * Commit atomically a group of {@link JournalEntry} which represents
     * one {@link TransactionEvent}.
     * Rollback all {@link JournalEntry} if any one is not successfully committed.
     * Throw run time error {@link InvalidBusinessRuleException} if
     * sum of amounts of all {@link JournalEntry} to commit does not equal to ZERO.
     * @param logsToCommit the atomic group of {@link JournalEntry}
     * @param kafkaOffset
     * @param kafkaPartition
     * @return successfully committed group of {@link JournalEntry}
     */
    Iterable<JournalEntry> commit(Iterable<JournalEntry> logsToCommit, Long kafkaOffset, Integer kafkaPartition);
}

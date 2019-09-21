package com.industrieit.ledger.clientledger.core.db.repository;


import com.industrieit.ledger.clientledger.core.db.entity.Account;
import com.industrieit.ledger.clientledger.core.db.entity.JournalEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;


@Repository
public interface JournalEntryRepository extends CrudRepository<JournalEntry, String> {
    Iterable<JournalEntry> findAllByAccount(Account account);

    Iterable<JournalEntry> findAllByAccountAndCreateTimeGreaterThanEqual(Account account, Timestamp timestamp);

    Iterable<JournalEntry> findAllByAccountAndCreateTimeGreaterThanEqualAndCreateTimeLessThan(Account account, Timestamp currentTimestamp, Timestamp lastTimestamp);

    @Override
    <S extends JournalEntry> Iterable<S> saveAll(Iterable<S> iterable);

    Iterable<JournalEntry> findAllByRequestId(String requestId);
}

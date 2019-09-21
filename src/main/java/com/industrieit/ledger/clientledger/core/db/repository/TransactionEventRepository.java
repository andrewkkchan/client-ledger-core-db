package com.industrieit.ledger.clientledger.core.db.repository;

import com.industrieit.ledger.clientledger.core.db.entity.TransactionEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionEventRepository extends CrudRepository<TransactionEvent, String> {
    Optional<TransactionEvent> findTop1ByOrderByKafkaOffsetDesc();
}

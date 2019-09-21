package com.industrieit.ledger.clientledger.core.db.repository;

import com.industrieit.ledger.clientledger.core.db.entity.TransactionResult;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionResultRepository extends CrudRepository<TransactionResult, String> {
    Optional<TransactionResult> findByRequestId(String requestId);

    Optional<TransactionResult> findTop1ByOrderByKafkaOffsetDesc();

    boolean existsByRequestId(String requestId);
}

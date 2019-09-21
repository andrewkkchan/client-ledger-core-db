package com.industrieit.ledger.clientledger.core.db.repository;

import com.industrieit.ledger.clientledger.core.db.entity.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {
    @Override
    Optional<Account> findById(String s);
}

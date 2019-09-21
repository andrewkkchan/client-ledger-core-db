package com.industrieit.ledger.clientledger.core.db.service;

import com.industrieit.ledger.clientledger.core.db.model.request.impl.CreateAccountRequest;
import com.industrieit.ledger.clientledger.core.db.entity.Account;

/**
 * Serializable isolated transactional service to mutate {@link Account}
 */
public interface AccountService {
    /**
     * Create an {@link Account} in serialized isolated transaction
     * @param createAccountRequest all the info needed for creating {@link Account}
     * @return {@link Account} successfully created
     */
    Account createAccount(CreateAccountRequest createAccountRequest);

}

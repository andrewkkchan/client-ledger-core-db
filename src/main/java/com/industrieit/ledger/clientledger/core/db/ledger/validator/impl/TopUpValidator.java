package com.industrieit.ledger.clientledger.core.db.ledger.validator.impl;

import com.industrieit.ledger.clientledger.core.db.model.ledger.Itemizable;
import com.industrieit.ledger.clientledger.core.db.model.ledger.impl.TopUpItemizable;
import com.industrieit.ledger.clientledger.core.db.model.request.impl.TopUpRequest;
import com.industrieit.ledger.clientledger.core.db.entity.Account;
import com.industrieit.ledger.clientledger.core.db.exception.InvalidBusinessRuleException;
import com.industrieit.ledger.clientledger.core.db.ledger.validator.Validator;
import com.industrieit.ledger.clientledger.core.db.repository.AccountRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class TopUpValidator implements Validator<TopUpRequest> {
    private final AccountRepository accountRepository;

    public TopUpValidator(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Itemizable validate(String requestId, TopUpRequest request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidBusinessRuleException("Negative amount not supported");
        }
        Optional<Account> topUpAccountOptional = accountRepository.findById(request.getTopUpAccount());
        if (!topUpAccountOptional.isPresent()) {
            throw new InvalidBusinessRuleException("Top-up account not found");
        } else {
            if (!topUpAccountOptional.get().getCurrency().equals(request.getCurrency())) {
                throw new InvalidBusinessRuleException("Currency exchange not supported for top-up account");
            }
        }
        Optional<Account> settlementAccountOptional = accountRepository.findById(request.getSettlementAccount());
        if (!settlementAccountOptional.isPresent()) {
            throw new InvalidBusinessRuleException("Settlement account not found");
        } else {
            if (!settlementAccountOptional.get().getCurrency().equals(request.getCurrency())) {
                throw new InvalidBusinessRuleException("Currency exchange not supported for settlement account");
            }
        }
        return new TopUpItemizable(topUpAccountOptional.get(), settlementAccountOptional.get(), request.getAmount(), request.getCurrency(), requestId);
    }
}

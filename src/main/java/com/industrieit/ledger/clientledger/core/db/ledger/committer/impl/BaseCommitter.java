package com.industrieit.ledger.clientledger.core.db.ledger.committer.impl;

import com.industrieit.ledger.clientledger.core.db.entity.Account;
import com.industrieit.ledger.clientledger.core.db.entity.JournalEntry;
import com.industrieit.ledger.clientledger.core.db.exception.InvalidBusinessRuleException;
import com.industrieit.ledger.clientledger.core.db.ledger.committer.Committer;
import com.industrieit.ledger.clientledger.core.db.repository.AccountRepository;
import com.industrieit.ledger.clientledger.core.db.repository.JournalEntryRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;

@Component
public class BaseCommitter implements Committer {
    private final JournalEntryRepository journalEntryRepository;
    private final AccountRepository accountRepository;


    public BaseCommitter(JournalEntryRepository journalEntryRepository, AccountRepository accountRepository) {
        this.journalEntryRepository = journalEntryRepository;
        this.accountRepository = accountRepository;
    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Iterable<JournalEntry> commit(Iterable<JournalEntry> logsToCommit, Long kafkaOffset, Integer kafkaPartition) {
        if (logsToCommit == null){
            return new ArrayList<>();
        }
        BigDecimal cumulativeAmount = BigDecimal.ZERO;
        for (JournalEntry journalEntry : logsToCommit){
            cumulativeAmount = cumulativeAmount.add(journalEntry.getAmount());
        }
        if (cumulativeAmount.compareTo(BigDecimal.ZERO) != 0){
            throw new InvalidBusinessRuleException("Unbalanced journal entries");
        }
        for (JournalEntry journalEntry : logsToCommit) {
            Account account = journalEntry.getAccount();
            account.setBalance(account.getBalance().add(journalEntry.getAmount()));
            account.setKafkaOffset(kafkaOffset);
            account.setKafkaPartition(kafkaPartition);
            accountRepository.save(account);
        }
        return journalEntryRepository.saveAll(logsToCommit);
    }
}

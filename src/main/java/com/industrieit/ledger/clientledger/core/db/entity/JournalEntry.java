package com.industrieit.ledger.clientledger.core.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * One of the only TWO building block for ledger, apart from {@link Account}
 * Represents the only allowable actions to be applied onto any {@link Account}
 * Summing up all {@link JournalEntry} for all {@link Account} in the ledger will always amount to ZERO.
 */
@Entity
@Table(name = "journal_entry")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class JournalEntry {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String requestId;

    /**
     * @return request ID which drives the posting of this journal entry, used for debug and tracing
     */
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "accountId")
    private Account account;

    private String currency;

    /**
     * @return currency code which the journal entry is denominated in, not necessary but just handy info.
     * Because currency is determined by {@link Account}
     * Each {@link Account} shall have exactly one currency.
     * If a customer needs many currency, just create many {@link Account}
     */
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @CreationTimestamp
    private Timestamp createTime;

    private BigDecimal amount;

    /**
     * @return id for database persisting, not particularly useful
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return {@link Account} which this journal entry is posted to
     */
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * @return create Time
     */
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }


    /**
     * @return Amount of the journal entry. Can be either positive and negative.
     * amount of one atomic block of journal entries shall also sum up to zero.
     */
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public JournalEntry(Account account, String currency, BigDecimal amount, String requestId) {
        this.account = account;
        this.amount = amount;
        this.currency = currency;
        this.requestId = requestId;
    }

    public JournalEntry() {
    }
}

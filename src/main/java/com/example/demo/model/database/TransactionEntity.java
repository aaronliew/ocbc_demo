package com.example.demo.model.database;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transaction")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "sender_user_id")
    Long senderUserId;

    @Column(name = "recipient_user_id")
    Long recipientUserId;

    @Column(name = "amount")
    Long amount;

    @Column(name = "transaction_date")
    @CreationTimestamp
    Date transactionDate;

    @Column(name = "record_create_date")
    @CreationTimestamp
    Date recordCreateDate;

    @Column(name = "record_update_date")
    @CreationTimestamp
    Date recordUpdateDate;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(Long senderUserId) {
        this.senderUserId = senderUserId;
    }

    public Long getRecipientUserId() {
        return recipientUserId;
    }

    public void setRecipientUserId(Long recipientUserId) {
        this.recipientUserId = recipientUserId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Date getRecordCreateDate() {
        return recordCreateDate;
    }

    public void setRecordCreateDate(Date recordCreateDate) {
        this.recordCreateDate = recordCreateDate;
    }

    public Date getRecordUpdateDate() {
        return recordUpdateDate;
    }

    public void setRecordUpdateDate(Date recordUpdateDate) {
        this.recordUpdateDate = recordUpdateDate;
    }
}

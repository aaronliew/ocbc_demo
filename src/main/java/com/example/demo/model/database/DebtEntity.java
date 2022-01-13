package com.example.demo.model.database;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "debt")
public class DebtEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "transaction_id")
    Long transactionId;

    @Column(name = "sender_user_id")
    Long senderUserId;

    @Column(name = "recipient_user_id")
    Long recipientUserId;

    @Column(name = "amount")
    Long amount;

    @Column(name = "record_create_date")
    @CreationTimestamp
    Date recordCreateDate;

    @Column(name = "record_update_date")
    @CreationTimestamp
    Date recordUpdateDate;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
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

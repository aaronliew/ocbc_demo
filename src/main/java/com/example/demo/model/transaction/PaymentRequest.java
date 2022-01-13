package com.example.demo.model.transaction;

import lombok.Data;

@Data
public class PaymentRequest {
    private long senderUserId;
    private long recipientUserId;
    private long amount;
}

package com.example.demo.model.transaction;

import lombok.Data;

@Data
public class Payment {
    private String senderName;
    private String recipientName;
    private long amount;
}

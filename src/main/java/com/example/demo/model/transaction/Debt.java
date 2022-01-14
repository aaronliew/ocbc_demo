package com.example.demo.model.transaction;

import lombok.Data;

@Data
public class Debt {
    private String senderName;
    private String recipientName;
    private long amount;
}

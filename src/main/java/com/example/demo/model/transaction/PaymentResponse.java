package com.example.demo.model.transaction;

import lombok.Data;

@Data
public class PaymentResponse {
    private long transaction;
    private long balance;
    private long debt;
    private String asOnDate;
}

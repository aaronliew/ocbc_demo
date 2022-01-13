package com.example.demo.service;

import com.example.demo.exception.InvalidArgumentException;
import com.example.demo.model.auth.LoginRequest;
import com.example.demo.model.auth.LoginResponse;
import com.example.demo.model.transaction.PaymentRequest;
import com.example.demo.model.transaction.PaymentResponse;

public interface TransactionService {
    PaymentResponse makePayment(PaymentRequest paymentRequest) throws InvalidArgumentException;
}

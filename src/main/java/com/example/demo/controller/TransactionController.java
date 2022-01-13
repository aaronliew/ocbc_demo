package com.example.demo.controller;

import com.example.demo.model.transaction.PaymentRequest;
import com.example.demo.model.transaction.PaymentResponse;
import com.example.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("transaction")
public class TransactionController {

    TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @PostMapping("/pay")
    public PaymentResponse makePayment(@RequestBody PaymentRequest paymentRequest){
        return transactionService.makePayment(paymentRequest);
    }


}

package com.example.demo.controller;

import com.example.demo.exception.InvalidArgumentException;
import com.example.demo.exception.InvalidRecipientException;
import com.example.demo.model.transaction.PaymentRequest;
import com.example.demo.model.transaction.PaymentResponse;
import com.example.demo.model.transaction.TopUpRequest;
import com.example.demo.model.transaction.TopUpResponse;
import com.example.demo.service.TransactionService;
import com.example.demo.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
        try {
            validateMakePaymentRequest(paymentRequest);
            paymentRequest.setSenderUserName(paymentRequest.getSenderUserName().toLowerCase());
            paymentRequest.setRecipientUserName(paymentRequest.getRecipientUserName().toLowerCase());
            return transactionService.makePayment(paymentRequest);
        } catch (InvalidRecipientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid recipient");
        } catch (InvalidArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid argument");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid argument");
        }
    }

    @PostMapping("/topup")
    public TopUpResponse topUp(@RequestBody TopUpRequest topUpRequest){
        try {
            validateTopUpRequest(topUpRequest);
            topUpRequest.setUsername(topUpRequest.getUsername().toLowerCase());
            return transactionService.topUpPayment(topUpRequest);
        } catch (InvalidArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid argument");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid argument");
        }

    }

    public void validateMakePaymentRequest(PaymentRequest paymentRequest) throws InvalidArgumentException {
        if (ValidationUtil.isStringEmpty(paymentRequest.getSenderUserName())) {
            throw new InvalidArgumentException();
        }

        if (ValidationUtil.isStringEmpty(paymentRequest.getRecipientUserName())) {
            throw new InvalidArgumentException();
        }

        if (paymentRequest.getSenderUserName().equals(paymentRequest.getRecipientUserName())){
            throw new InvalidRecipientException();
        }

        if (ValidationUtil.isLongValueEmpty(paymentRequest.getAmount())) {
            throw new InvalidArgumentException();
        }
    }

    public void validateTopUpRequest(TopUpRequest topUpRequest) throws InvalidArgumentException {
        if (ValidationUtil.isStringEmpty(topUpRequest.getUsername())) {
            throw new InvalidArgumentException();
        }

        if (ValidationUtil.isLongValueEmpty(topUpRequest.getAmount())) {
            throw new InvalidArgumentException();
        }
    }

}

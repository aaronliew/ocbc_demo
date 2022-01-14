package com.example.demo.rest;

import com.example.demo.exception.ApiException;
import com.example.demo.model.ErrorResponse;
import com.example.demo.model.auth.LoginRequest;
import com.example.demo.model.auth.LoginResponse;
import com.example.demo.model.transaction.PaymentRequest;
import com.example.demo.model.transaction.PaymentResponse;
import com.example.demo.model.transaction.TopUpRequest;
import com.example.demo.model.transaction.TopUpResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public class TransactionApi {

    public static PaymentResponse pay(String baseUrl, PaymentRequest paymentRequest) throws ApiException{
        HttpResponse<PaymentResponse> response = Unirest.post(baseUrl+"transaction/pay")
                .header("Content-Type", "application/json")
                .body(paymentRequest)
                .asObject(PaymentResponse.class);

        if (!response.isSuccess()) {
            ErrorResponse errorResponse = response.mapError(ErrorResponse.class);
            throw new ApiException(errorResponse);
        }

        return response.getBody();
    }

    public static TopUpResponse topUp(String baseUrl, TopUpRequest topUpRequest) throws ApiException{
        HttpResponse<TopUpResponse> response = Unirest.post(baseUrl+"transaction/topup")
                .header("Content-Type", "application/json")
                .body(topUpRequest)
                .asObject(TopUpResponse.class);

        if (!response.isSuccess()) {
            ErrorResponse errorResponse = response.mapError(ErrorResponse.class);
            throw new ApiException(errorResponse);
        }

        return response.getBody();
    }
}

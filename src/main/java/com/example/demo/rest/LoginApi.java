package com.example.demo.rest;

import com.example.demo.model.auth.LoginRequest;
import com.example.demo.model.auth.LoginResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

public class LoginApi {

    public static LoginResponse login(String baseUrl, LoginRequest loginRequest) {
        HttpResponse<LoginResponse> response = Unirest.post(baseUrl+"auth/login")
                .header("Content-Type", "application/json")
                .body(loginRequest)
                .asObject(LoginResponse.class);

       return response.getBody();
    }
}

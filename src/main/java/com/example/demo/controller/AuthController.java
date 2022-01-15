package com.example.demo.controller;

import com.example.demo.model.auth.LoginRequest;
import com.example.demo.model.auth.LoginResponse;
import com.example.demo.model.transaction.Debt;
import com.example.demo.service.AuthService;
import com.example.demo.exception.InvalidArgumentException;
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
@RequestMapping("auth")
public class AuthController {

    AuthService authService;
    TransactionService transactionService;

    @Autowired
    public AuthController(AuthService authService, TransactionService transactionService){
        this.authService = authService;
        this.transactionService = transactionService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest){
        try {
            validateLoginRequest(loginRequest);
            loginRequest.setUsername(loginRequest.getUsername().toLowerCase());
            LoginResponse loginResponse = authService.login(loginRequest);
            Debt debt = transactionService.getDebtSummary(loginResponse.getId());
            loginResponse.setDebt(debt);
            return loginResponse;
        } catch (InvalidArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid argument");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid argument");
        }
    }

    public void validateLoginRequest(LoginRequest loginRequest) throws InvalidArgumentException {
        if (ValidationUtil.isStringEmpty(loginRequest.getUsername())) {
            throw new InvalidArgumentException();
        }
    }




}

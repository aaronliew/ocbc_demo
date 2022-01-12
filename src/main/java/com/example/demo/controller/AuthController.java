package com.example.demo.controller;

import com.example.demo.model.auth.LoginRequest;
import com.example.demo.model.auth.LoginResponse;
import com.example.demo.service.AuthService;
import com.example.demo.exception.InvalidArgumentException;
import com.example.demo.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {

    AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest){
        validateLoginRequest(loginRequest);
        return authService.login(loginRequest);
    }

    public void validateLoginRequest(LoginRequest loginRequest) throws InvalidArgumentException {
        if (ValidationUtil.isStringEmpty(loginRequest.getUsername())) {
            throw new InvalidArgumentException();
        }
    }




}

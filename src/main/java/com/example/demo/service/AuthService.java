package com.example.demo.service;

import com.example.demo.model.auth.LoginRequest;
import com.example.demo.model.auth.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
}

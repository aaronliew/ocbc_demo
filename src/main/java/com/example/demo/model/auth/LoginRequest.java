package com.example.demo.model.auth;

import lombok.Data;

@Data
public class LoginRequest {
    String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

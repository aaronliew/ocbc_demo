package com.example.demo.service;

import com.example.demo.database.AuthDao;
import com.example.demo.model.auth.LoginRequest;
import com.example.demo.model.auth.LoginResponse;
import com.example.demo.model.database.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class AuthServiceImpl implements AuthService{

    AuthDao authDao;

    @Autowired
    public AuthServiceImpl(AuthDao authDao){
        this.authDao = authDao;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest){
        String username = loginRequest.getUsername().toLowerCase();
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setBalance(0L);

        UserEntity result = authDao.getUserEntitiesByUsername(username);
        if (result == null) {
           result = authDao.save(userEntity);
        }

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUsername(result.getUsername());
        loginResponse.setMessage("Hello, " + result.getUsername());

        return loginResponse;
    }
}

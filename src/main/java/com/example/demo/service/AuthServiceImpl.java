package com.example.demo.service;

import com.example.demo.database.AuthDao;
import com.example.demo.model.auth.LoginRequest;
import com.example.demo.model.auth.LoginResponse;
import com.example.demo.model.database.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{

    AuthDao authDao;

    @Autowired
    public AuthServiceImpl(AuthDao authDao){
        this.authDao = authDao;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(loginRequest.getUsername());

        UserEntity result = authDao.getUserEntitiesByUsername(loginRequest.getUsername());
        if (result == null) {
           result = authDao.save(userEntity);
        }

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUsername(result.getUsername());
        loginResponse.setMessage("Hello, " + result.getUsername());

        return loginResponse;
    }
}

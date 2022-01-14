package com.example.demo.model.auth;

import com.example.demo.model.transaction.Debt;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LoginResponse {
    private Long id;
    private String username;
    private Debt debt;
}

package com.example.demo.exception;

import com.example.demo.model.ErrorResponse;

public class ApiException extends RuntimeException{

    private ErrorResponse errorResponse;
    public ApiException(ErrorResponse errorResponse){
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}

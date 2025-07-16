package com.register.DTO.Response;

import java.util.Map;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class UserValidationResponse implements CustomResponse {
    private String message;
    private int status;
    private Map<String, String> fieldErrors;

    public UserValidationResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public UserValidationResponse(String message, int status, Map<String, String> fieldErrors) {
        this.message = message;
        this.status = status;
        this.fieldErrors = fieldErrors;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

}

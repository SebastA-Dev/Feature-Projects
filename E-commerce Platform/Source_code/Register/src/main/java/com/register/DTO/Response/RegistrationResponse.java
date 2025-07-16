package com.register.DTO.Response;

import java.util.Map;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Introspected
@Serdeable
public class RegistrationResponse implements CustomResponse {
    private String message;
    private int status;
    private Map<String, String> fieldErrors;

    /**
     * Default constructor for RegistrationResponse
     */
    public RegistrationResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    /**
     * Constructor for RegistrationResponse with field errors
     * 
     * @param status      status code
     * @param message     message to be displayed
     * @param fieldErrors map of field errors
     */
    public RegistrationResponse(int status, String message, Map<String, String> fieldErrors) {
        this.status = status;
        this.message = message;
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

package com.register.DTO.Request;

import io.micronaut.core.annotation.Introspected;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Introspected
public class UserValidationRequest {

    @NotNull
    @Email(message = "Email should be valid")
    private String email;

    @NotNull
    @Size(min = 6, max = 6, message = "Code must be exactly 6 digits")
    private int code;

    public UserValidationRequest(String email, int code) {
        this.email = email;
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}

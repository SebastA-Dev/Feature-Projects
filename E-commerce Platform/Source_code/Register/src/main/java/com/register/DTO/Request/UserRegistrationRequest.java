package com.register.DTO.Request;

import io.micronaut.core.annotation.Introspected;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Introspected
public class UserRegistrationRequest {

    @NotNull
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    public String name;

    @NotNull
    @Size(min = 3, max = 50, message = "Surname must be between 3 and 50 characters")
    public String surname;

    @NotNull
    @Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters")
    public String username;

    @NotNull
    @Email(message = "Email should be valid")
    public String email;

    @NotNull
    @Size(min = 9, max = 50)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and be at least 8 characters long")
    public String password;

    @NotNull
    @Size(min = 9, max = 50)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and be at least 8 characters long")
    public String confirmPassword;

    @NotNull
    public String phoneNumber;

    // Create 2 getters, one for the password and one for the confirmPassword
    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public void deletePassword() {
        this.password = null;
        this.confirmPassword = null;
    }
}

package com.register.DTO.Request;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * A data transfer object (DTO) used to capture login request information from
 * the client.
 * Includes validation constraints to ensure input integrity.
 */
@Introspected
@Serdeable.Deserializable
public class LoginRequest {

    /**
     * The username of the user.
     * Must be between 1 and 50 characters.
     */
    @Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters")
    private String username;

    /**
     * The email address of the user.
     * Must be a valid email format.
     */
    @Email(message = "Email should be valid")
    private String email;

    /**
     * The user's password.
     * Must contain at least one uppercase letter, one lowercase letter, one digit,
     * and be at least 8 characters long.
     */
    @Size(min = 9, max = 50)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and be at least 8 characters long")
    private String password;

    /**
     * The user's password.
     * Must contain at least one uppercase letter, one lowercase letter, one digit,
     * and be at least 8 characters long.
     */
    @Size(min = 9, max = 50)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and be at least 8 characters long")
    private String passwordConfirmation;

    /**
     * Gets the username.
     *
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the email address.
     *
     * @return the email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password.
     *
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the password.
     *
     * @return the password of the user
     */
    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }
}

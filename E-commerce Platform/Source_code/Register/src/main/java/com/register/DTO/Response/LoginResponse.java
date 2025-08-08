package com.register.DTO.Response;

import java.util.Map;

/**
 * Represents the response returned after a login attempt.
 * This class implements {@link CustomResponse} and can represent both success
 * and failure outcomes.
 */
public class LoginResponse implements CustomResponse {

    /** A message describing the result of the login operation. */
    private String message;

    /** The authentication token returned on successful login. */
    private String token;

    /** The duration (in seconds or minutes) the token is valid for. */
    private double tokenDuration;

    /** A token used to refresh the authentication session. */
    private String refreshToken;

    /** The HTTP-like status code representing the result of the login request. */
    private int status;

    /**
     * Optional field-level errors if the login attempt failed due to invalid
     * inputs.
     */
    private Map<String, String> fieldErrors;

    /**
     * Constructs a successful {@code LoginResponse} with token information.
     *
     * @param message       a description message
     * @param status        the response status code
     * @param tokenDuration duration of the access token
     * @param refreshToken  refresh token string
     * @param token         access token string
     */
    public LoginResponse(String message, int status, double tokenDuration, String refreshToken, String token) {
        this.message = message;
        this.status = status;
        this.tokenDuration = tokenDuration;
        this.refreshToken = refreshToken;
        this.token = token;
    }

    /**
     * Constructs a failed {@code LoginResponse} with field-level validation errors.
     *
     * @param message     a description message
     * @param status      the response status code
     * @param fieldErrors a map of field names to their corresponding validation
     *                    error messages
     */
    public LoginResponse(String message, int status, Map<String, String> fieldErrors) {
        this.message = message;
        this.status = status;
        this.fieldErrors = fieldErrors;
    }

    /**
     * Gets the response message.
     *
     * @return the response message
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * Sets the response message.
     *
     * @param message the response message to set
     */
    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the status code of the response.
     *
     * @return the status code
     */
    @Override
    public int getStatus() {
        return status;
    }

    /**
     * Sets the status code of the response.
     *
     * @param status the status code to set
     */
    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Gets the authentication token.
     *
     * @return the access token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the authentication token.
     *
     * @param token the access token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Gets the duration for which the token is valid.
     *
     * @return the token duration
     */
    public double getTokenDuration() {
        return tokenDuration;
    }

    /**
     * Sets the duration for which the token is valid.
     *
     * @param tokenDuration the token duration to set
     */
    public void setTokenDuration(double tokenDuration) {
        this.tokenDuration = tokenDuration;
    }

    /**
     * Gets the refresh token used for renewing sessions.
     *
     * @return the refresh token
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * Sets the refresh token used for renewing sessions.
     *
     * @param refreshToken the refresh token to set
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * Gets the map of field-level validation errors.
     *
     * @return a map containing field names and their associated error messages
     */
    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    /**
     * Sets the map of field-level validation errors.
     *
     * @param fieldErrors a map of field names to error messages
     */
    public void setFieldErrors(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}

package com.register.Services;

import java.util.HashMap;
import java.util.Map;

import com.register.DB.Repository.UserRepository;
import com.register.DTO.Request.UserRegistrationRequest;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/**
 * Service class for managing user-related operations.
 */
@Singleton
public class UserService {

    private String hashedPassword;

    @Inject
    private HashService hashService;

    @Inject
    private UserRepository userRepository;

    // -------------------------------------------------------------------------
    // User Registration
    // -------------------------------------------------------------------------

    /**
     * Registers a user in the database.
     *
     * @param userRegistrationRequest The user registration request object.
     * @param userIpAddress           The IP address of the user.
     * @return Boolean True if the user is registered successfully, false otherwise.
     */
    public Boolean registerUser(UserRegistrationRequest userRegistrationRequest, String userIpAddress) {
        try {
            hashedPassword = hashService.hash(userRegistrationRequest.getPassword());
            userRepository.saveUser(userRegistrationRequest, hashedPassword, userIpAddress);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error while registering user: " + e.getMessage(), e);
        }
    }

    // -------------------------------------------------------------------------
    // User Existence Check
    // -------------------------------------------------------------------------

    /**
     * Checks if a user already exists in the database.
     *
     * @param userRegistrationData A map containing the username and email to check.
     * @return Map<String, Boolean> A map indicating whether the username and email
     *         exist.
     */
    public Map<String, Boolean> getUserExistence(Map<String, String> userRegistrationData) {
        String username = userRegistrationData.get("username");
        String email = userRegistrationData.get("email");

        boolean usernameExists = userRepository.findUserByUsername(username);
        boolean emailExists = userRepository.findUserByEmail(email);

        Map<String, Boolean> userExistence = new HashMap<>();
        userExistence.put("username", usernameExists);
        userExistence.put("email", emailExists);
        return userExistence;
    }

    // -------------------------------------------------------------------------
    // Validation Code Management
    // -------------------------------------------------------------------------

    /**
     * Updates the validation code for a user.
     *
     * @param email The user's email address.
     * @param code  The validation code to save.
     */
    public void updateUserValidationCode(String email, int code) {
        boolean userExists = userRepository.findUserByEmail(email);
        if (userExists) {
            userRepository.saveUserValidationCode(email, code);
        } else {
            throw new RuntimeException("User not found with email: " + email);
        }
    }

    /**
     * Deletes the validation code for a user.
     *
     * @param email The user's email address.
     */
    public void deleteUserValidationCode(String email) {
        boolean userExists = userRepository.findUserByEmail(email);
        if (userExists) {
            userRepository.deleteUserValidationCode(email);
        } else {
            throw new RuntimeException("User not found with email: " + email);
        }
    }

    /**
     * Retrieves the number of attempts a user has made to validate their code.
     *
     * @param email The user's email address.
     * @return int The number of validation attempts.
     */
    public int getUserValidationCodeTryCount(String email) {
        boolean userExists = userRepository.findUserByEmail(email);
        if (userExists) {
            return userRepository.getUserValidationCodeTryCount(email);
        } else {
            throw new RuntimeException("User not found with email: " + email);
        }
    }

    /**
     * Updates the number of validation attempts for a user.
     *
     * @param email    The user's email address.
     * @param tryCount The number of attempts to save.
     */
    public void updateUserValidationCodeTryCount(String email, int tryCount) {
        boolean userExists = userRepository.findUserByEmail(email);
        if (userExists) {
            userRepository.updateUserValidationTryCount(email, tryCount);
        } else {
            throw new RuntimeException("User not found with email: " + email);
        }
    }

    public String getValidationCodeInstanceTime(String email) {
        boolean userExists = userRepository.findUserByEmail(email);
        if (userExists) {
            return userRepository.getValidationCodeInstanceTime(email);
        } else {
            throw new RuntimeException("User not found with email: " + email);
        }
    }
}
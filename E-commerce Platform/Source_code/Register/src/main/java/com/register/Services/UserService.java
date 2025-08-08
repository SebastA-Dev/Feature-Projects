package com.register.Services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Dictionary;
import java.util.Map;

import org.springframework.cglib.core.Local;

import com.register.DB.Repository.UserRepository;
import com.register.DTO.Request.LoginRequest;
import com.register.DTO.Request.UserRegistrationRequest;

import io.micronaut.context.annotation.Value;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class UserService {

    @Inject
    private HashService hashService;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordService passwordService;

    @Value("${micronaut.session.durationToken}")
    private long sessionDuration;

    @Value("${micronaut.session.durationRefresh}")
    private long refreshTokenDuration;

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
            String hashedPassword = hashService.hash(userRegistrationRequest.getPassword());
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
    public boolean getUserExistence(Map<String, String> userRegistrationData) {
        String username = userRegistrationData.get("username");
        String email = userRegistrationData.get("email");

        boolean usernameExists = userRepository.findUserByUsername(username);
        boolean emailExists = userRepository.findUserByEmail(email);
        if (usernameExists) {
            return true;
        }
        if (emailExists) {
            return true;
        }
        return false;
    }

    /**
     * 
     * Checks if the user login matches any value in the DB.
     * 
     * @param login This param represents the user's login information.
     * @return
     */
    public boolean checkUserExistence(LoginRequest login) {
        Boolean passwordMatching = passwordService.verifyPassword(login.getPassword(),
                login.getPasswordConfirmation());

        if (!passwordMatching)
            return false;

        String hashedPassword = hashService.hash(login.getPassword());
        return userRepository.findUserByUsername(hashedPassword, hashedPassword);
    }

    /**
     * Save the user session information.
     * 
     * @param login This param represents the user's login information.
     */
    public void registerUserSession(LoginRequest login) {
        LocalDateTime creationDateTime = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime expiresDateTime = creationDateTime.plusMinutes(sessionDuration);
        LocalDateTime expiresDateTimeRefresh = creationDateTime.plusMinutes(refreshTokenDuration);
        userRepository.saveUserSession(login.getUsername(), login.getEmail(), creationDateTime, expiresDateTime,
                expiresDateTimeRefresh);
    }

    /**
     * Retrieves desired user UUID.
     * 
     * @param loginInformation This param represents the user's login information.
     * @return
     */
    public Dictionary<String, String> getUserUUID(LoginRequest loginInformation) {
        return userRepository.getUserUUID(loginInformation.getUsername(), loginInformation.getEmail());
    }

    // TODO: Implement the methods for the user validation, this should be happen
    // when the user-configuration and insights microservice are on construction and
    // ready

    // -------------------------------------------------------------------------
    // Validation Code Management
    // -------------------------------------------------------------------------

    // /**
    // * Updates the validation code for a user.
    // *
    // * @param email The user's email address.
    // * @param code The validation code to save.
    // */
    // public void updateUserValidationCode(String email, int code) {
    // boolean userExists = userRepository.findUserByEmail(email);
    // if (userExists) {
    // userRepository.saveUserValidationCode(email, code);
    // } else {
    // throw new RuntimeException("User not found with email: " + email);
    // }
    // }

    // /**
    // * Deletes the validation code for a user.
    // *
    // * @param email The user's email address.
    // */
    // public void deleteUserValidationCode(String email) {
    // boolean userExists = userRepository.findUserByEmail(email);
    // if (userExists) {
    // userRepository.saveUserValidationCode(email, 0); // Set validation code to 0
    // }
    // throw new RuntimeException("User not found with email: " + email);
    // }

    // /**
    // * Retrieves the number of attempts a user has made to validate their code.
    // *
    // * @param email The user's email address.
    // * @return int The number of validation attempts.
    // */
    // public int getUserValidationCodeTryCount(String email) {
    // boolean userExists = userRepository.findUserByEmail(email);
    // if (userExists) {
    // return userRepository.getUserValidationCode(email);
    // } else {
    // throw new RuntimeException("User not found with email: " + email);
    // }
    // }

    // /**
    // * Updates the number of validation attempts for a user.
    // *
    // * @param email The user's email address.
    // * @param tryCount The number of attempts to save.
    // */
    // public void updateUserValidationCodeTryCount(String email, int tryCount) {
    // boolean userExists = userRepository.findUserByEmail(email);
    // if (userExists) {
    // userRepository.saveUserValidationCode(email, tryCount);
    // } else {
    // throw new RuntimeException("User not found with email: " + email);
    // }
    // }

    // /**
    // * Retrieves the validation code instance time for a user.
    // *
    // * @param email The user's email address.
    // * @return int The validation code.
    // */
    // public int getValidationCodeInstanceTime(String email) {
    // boolean userExists = userRepository.findUserByEmail(email);
    // if (userExists) {
    // return userRepository.getUserValidationCode(email);
    // } else {
    // throw new RuntimeException("User not found with email: " + email);
    // }
    // }

}

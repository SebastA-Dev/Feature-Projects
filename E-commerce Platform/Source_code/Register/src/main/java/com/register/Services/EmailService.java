package com.register.Services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.register.DB.Repository.UserRepository;
import com.register.DTO.Request.UserValidationRequest;
import com.register.DTO.Response.UserValidationResponse;

import io.micronaut.email.BodyType;
import io.micronaut.email.Email;
import io.micronaut.email.EmailSender;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class EmailService {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private final EmailSender<?, ?> emailSender;

    @Inject
    private UserRepository userRepository;

    public EmailService(EmailSender<?, ?> emailSender) {
        this.emailSender = emailSender;
    }

    /**
     * Sends a validation code email to the user.
     *
     * @param email The user's email address.
     */
    public void sendValidationCode(String email) {
        if (!isValid(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        int code = generateValidationCode();
        userRepository.saveUserValidationCode(email, code);

        Email.Builder emailBuilder = Email.builder()
                .to(email)
                .subject("Your Verification Code")
                .body("Dear user,\n\nYour verification code is: " + code + "\n\nThank you.", BodyType.TEXT);

        emailSender.send(emailBuilder);
    }

    /**
     * Resends the validation code to the user's email address.
     *
     * @param email The user's email address.
     */
    public void resendValidationCode(String email) {
        int code = generateValidationCode();
        int attempts = userRepository.getUserValidationCodeTryCount(email) - 1;

        userRepository.saveUserValidationCode(email, code);
        userRepository.updateUserValidationTryCount(email, attempts);

        sendValidationCode(email);
    }

    /**
     * Validates the email code entered by the user.
     *
     * @param userValidationRequest The request containing the email and validation
     *                              code.
     * @return UserValidationResponse The response indicating success or failure.
     */
    public UserValidationResponse validateEmailCode(UserValidationRequest userValidationRequest) {
        int code = userRepository.getUserValidationCode(userValidationRequest.getEmail());
        String instanceTime = userRepository.getValidationCodeInstanceTime(userValidationRequest.getEmail());

        Map<String, String> fieldErrors = new HashMap<>();

        if (code == -1) {
            fieldErrors.put("code", "Code doesn't exist");
            return new UserValidationResponse("The code doesn't exist or is incorrect", 400, fieldErrors);
        }

        if (code != userValidationRequest.getCode()) {
            fieldErrors.put("code", "Code doesn't match");
            return new UserValidationResponse("The code doesn't match", 400, fieldErrors);
        }

        if (instanceTime != null) {
            try {
                LocalDateTime codeTime = LocalDateTime.parse(instanceTime,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                LocalDateTime currentTime = LocalDateTime.now();

                if (Duration.between(codeTime, currentTime).toMinutes() > 5) {
                    fieldErrors.put("code", "Code has expired");
                    return new UserValidationResponse("The code has expired", 400, fieldErrors);
                }
            } catch (Exception e) {
                fieldErrors.put("instanceTime", "Invalid instance time format");
                return new UserValidationResponse("Error parsing instance time", 500, fieldErrors);
            }
        } else {
            fieldErrors.put("instanceTime", "Instance time not found");
            return new UserValidationResponse("Instance time not found", 400, fieldErrors);
        }

        return new UserValidationResponse("Code validated successfully", 200, null);
    }

    /**
     * Checks if an email is valid.
     *
     * @param email The email to validate.
     * @return True if the email is valid, false otherwise.
     */
    public Boolean isValid(String email) {
        return email.matches(EMAIL_REGEX);
    }

    /**
     * Generates a random 6-digit validation code.
     *
     * @return int The generated code.
     */
    private int generateValidationCode() {
        return (int) (Math.random() * 900000) + 100000; // Generates a 6-digit code
    }
}
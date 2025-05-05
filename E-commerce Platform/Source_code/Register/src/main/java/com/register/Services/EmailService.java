package com.register.Services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.register.DB.Repository.UserRepository;
import com.register.DTO.Request.UserValidationRequest;
import com.register.DTO.Response.UserValidationResponse;

import io.micronaut.email.Email;
import io.micronaut.email.BodyType;
import io.micronaut.email.EmailSender;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class EmailService {

    private final EmailSender<?, ?> emailSender;

    @Inject
    private UserRepository userRepository;

    public EmailService(EmailSender<?, ?> emailSender) {
        this.emailSender = emailSender;
    }

    /**
     * Envía un código de validación al correo del usuario.
     */
    public void sendValidationCode(String toAddress) {
        if (!isValid(toAddress)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        int code = generateValidationCode();
        userRepository.saveUserValidationCode(toAddress, code);

        Email.Builder emailBuilder = Email.builder()
                // Remitente obligatorio: puedes parametrizarlo en application.yml
                .from("no-reply@tu-dominio.com")
                .to(toAddress)
                .subject("Your Verification Code")
                .body(
                        "Dear user,\n\nYour verification code is: " + code + "\n\nThank you.",
                        BodyType.TEXT);

        // send acepta un Email.Builder
        emailSender.send(emailBuilder);
    }

    /**
     * Reenvía el código de validación (regenera y actualiza conteo de intentos).
     */
    public void resendValidationCode(String toAddress) {
        if (!isValid(toAddress)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        int code = generateValidationCode();
        userRepository.saveUserValidationCode(toAddress, code);

        // Opcional: actualizar aquí el contador de reintentos
        // userRepository.updateUserValidationTryCount(toAddress, newAttempts);

        // Reutiliza el método que construye y envía el email
        sendValidationCode(toAddress);
    }

    public UserValidationResponse validateEmailCode(UserValidationRequest req) {
        int codeStored = userRepository.getUserValidationCode(req.getEmail());
        String instanceTime = userRepository.getValidationCodeInstanceTime(req.getEmail());

        Map<String, String> fieldErrors = new HashMap<>();

        if (codeStored == -1) {
            fieldErrors.put("code", "Code doesn't exist");
            return new UserValidationResponse("The code doesn't exist or is incorrect", 400, fieldErrors);
        }

        if (codeStored != req.getCode()) {
            fieldErrors.put("code", "Code doesn't match");
            return new UserValidationResponse("The code doesn't match", 400, fieldErrors);
        }

        if (instanceTime != null) {
            try {
                LocalDateTime codeTime = LocalDateTime.parse(
                        instanceTime,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                if (Duration.between(codeTime, LocalDateTime.now()).toMinutes() > 5) {
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

    public Boolean isValid(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    private int generateValidationCode() {
        return (int) (Math.random() * 900000) + 100000;
    }
}

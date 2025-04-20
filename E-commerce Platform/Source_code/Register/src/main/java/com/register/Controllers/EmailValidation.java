package com.register.Controllers;

import java.util.HashMap;
import java.util.Map;

import com.register.DTO.Request.UserValidationRequest;
import com.register.DTO.Response.UserValidationResponse;
import com.register.Services.EmailService;
import com.register.Services.UserService;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.inject.Inject;
import jakarta.validation.Valid;

@Controller("/v1/api/")
@ConfigurationProperties("micronaut.security.token.email.validation")
public class EmailValidation {

    @Inject
    private UserService userService;

    @Inject
    private EmailService emailService;

    /**
     * Sends an email validation message to the user.
     *
     * @param email The user's email address.
     * @return HttpResponse<Map<String, Object>> The HTTP response containing the
     *         email headers and message.
     */
    @Get("get/user/email-validation")
    public HttpResponse<?> sendEmailValidation(@QueryValue String email) {
        try {
            emailService.sendValidationCode(email);
            return HttpResponse.ok(Map.of("message", "Validation code sent to your email."));
        } catch (Exception e) {
            return HttpResponse.serverError(Map.of("error", "Email sending failed."));
        }
    }

    /**
     * Resends the validation code to the user's email address.
     *
     * @param email The user's email address.
     * @return HttpResponse<Map<String, Object>> The HTTP response indicating
     *         success or failure.
     */
    @Get("get/user/resend-validation-code")
    public HttpResponse<?> resendValidationCode(@QueryValue String email) {
        try {
            emailService.resendValidationCode(email);
            return HttpResponse.ok(Map.of("message", "Code resent to your email."));
        } catch (Exception e) {
            return HttpResponse.serverError(Map.of("error", "Resending failed."));
        }
    }

    /**
     * Validates the email code entered by the user.
     *
     * @param userValidationRequest The request containing the email and validation
     *                              code.
     * @return UserValidationResponse The response indicating success or failure.
     */
    @Post("post/user/email/validation-code/")
    public UserValidationResponse validateEmailCode(@Valid @RequestBody UserValidationRequest userValidationRequest) {
        return emailService.validateEmailCode(userValidationRequest);
    }
}
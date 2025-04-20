package com.register.Configuration.ExceptionHandling;

import io.micronaut.http.server.exceptions.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import com.register.DTO.Response.RegistrationResponse;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@Produces
@Singleton
@Requires(classes = { ConstraintViolationException.class, ExceptionHandler.class })
public class ValidationExceptionHandling implements ExceptionHandler<ConstraintViolationException, HttpResponse<?>> {

    @Override
    public HttpResponse<?> handle(HttpRequest request, ConstraintViolationException exception) {
        Map<String, String> fieldErrors = new HashMap<>();

        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            fieldErrors.put(propertyPath, message);
        }

        RegistrationResponse errorResponse = new RegistrationResponse(
                HttpStatus.BAD_REQUEST.getCode(),
                "Validation failed for the request",
                fieldErrors);

        return HttpResponse.badRequest(errorResponse);
    }
}

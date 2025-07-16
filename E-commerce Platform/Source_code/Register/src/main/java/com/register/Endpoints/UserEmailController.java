package com.register.Endpoints;

import com.register.Services.EmailService;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;

@Controller("/v1/api/")
public class UserEmailController {

    private final EmailService emailService;

    // TODO: complete this controller to handle email sending requests |
    // Microservice user-configuration and insights should be finished

    public UserEmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @Get("/email-test/send")
    public String sendEmail(@QueryValue String to) {
        String subject = "Correo de prueba";
        String body = "Este es un correo de prueba enviado desde Micronaut con Gmail SMTP.";

        boolean sent = emailService.sendEmail(to, subject, body);
        return sent ? "Correo enviado correctamente a " + to : "Error al enviar correo a " + to;
    }
}
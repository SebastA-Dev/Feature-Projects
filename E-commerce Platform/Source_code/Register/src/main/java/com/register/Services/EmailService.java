package com.register.Services;

import jakarta.inject.Singleton;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

import io.micronaut.context.annotation.Value;

/**
 * Service responsible for sending emails using SMTP via Gmail.
 * This class uses Jakarta Mail (JavaMail) and is managed by Micronaut as a
 * singleton bean.
 */
@Singleton
public class EmailService {

    /**
     * Email address used to send messages (configured via application properties).
     */
    @Value("${EMAIL_USERNAME}")
    protected String email;

    /** Password or App Password for the configured email account. */
    @Value("${EMAIL_PASSWORD}")
    protected String password;

    /** JavaMail session used to manage authentication and SMTP properties. */
    private Session session;

    /**
     * Constructor that initializes email credentials and configures the mail
     * session.
     *
     * @param email    Email address used for sending messages.
     * @param password Password or app-password for the email account.
     */
    public EmailService(@Value("${EMAIL_USERNAME}") String email,
            @Value("${EMAIL_PASSWORD}") String password) {
        this.email = email;
        this.password = password;
        this.session = configureMailSession();
    }

    /**
     * Configures and returns a JavaMail Session with SMTP settings.
     * Gmail SMTP server is used on port 587 with TLS.
     *
     * @return A configured JavaMail session instance.
     */
    protected Session configureMailSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });
    }

    /**
     * Builds and returns a MimeMessage with the specified recipient, subject, and
     * body.
     *
     * @param to      The recipient's email address.
     * @param subject The subject line of the email.
     * @param body    The body content of the email.
     * @return A ready-to-send MimeMessage.
     * @throws MessagingException If any part of the message building fails.
     */
    private Message constructEmailMessage(String to, String subject, String body) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(email));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(body);
        return message;
    }

    /**
     * Sends an email to a recipient with the specified subject and body.
     *
     * @param to      The recipient's email address.
     * @param subject The subject line of the email.
     * @param body    The body content of the email.
     * @return True if the email is sent successfully; false otherwise.
     */
    public boolean sendEmail(String to, String subject, String body) {
        if (email == null || password == null) {
            System.err.println("Credenciales no encontradas");
            return false;
        }

        try {
            Message message = constructEmailMessage(to, subject, body);
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}

package com.register.Services;

import jakarta.inject.Singleton;

//TODO: implement the EmailService class with necessary fields and methods
@Singleton
public class EmailService {

    // TODO: implement the EmailService class with necessary fields and methods
    // Regex pattern for validating email addresses
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    // TODO: implement the EmailService class with necessary fields and methods
    public Boolean isValid(String email) {
        // email regex pattern
        return false;
    }

    // TODO: implement the EmailService class with necessary fields and methods
    public Boolean exist(String email) {
        // check if email exists in the database
        return false;
    }

}

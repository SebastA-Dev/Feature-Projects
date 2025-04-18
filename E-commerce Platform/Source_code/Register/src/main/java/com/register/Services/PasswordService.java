package com.register.Services;

import jakarta.inject.Singleton;
import java.util.regex.Pattern;

@Singleton
public class PasswordService {

    private static final Pattern STRONG_PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");

    // TODO: implement the PasswordService class with necessary fields and methods
    public boolean isStrong(String password) {
        return STRONG_PASSWORD_PATTERN.matcher(password).matches();
    }

    // TODO: implement the PasswordService class with necessary fields and methods
    public boolean verifyPassword(String password, String hashedPassword) {
        // Implement your password verification logic here
        return password.equals(hashedPassword); // Placeholder, replace with actual verification
    }

}

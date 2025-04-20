package com.register.Services;

import de.mkammerer.argon2.Argon2;

import jakarta.inject.Singleton;
import java.util.regex.Pattern;

@Singleton
public class PasswordService {

    private static final Pattern STRONG_PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");

    /**
     * Hashes a password using Argon2 algorithm.
     *
     * @param password       the password to hash
     * @param hashedPassword the hashed password to compare against
     * @return the hashed password
     */
    public boolean verifyPassword(String password, String hashedPassword) {
        Argon2 argon2 = de.mkammerer.argon2.Argon2Factory.create();
        try {
            return argon2.verify(hashedPassword, password.toCharArray());
        } finally {
            argon2.wipeArray(password.toCharArray());
        }
    }

}

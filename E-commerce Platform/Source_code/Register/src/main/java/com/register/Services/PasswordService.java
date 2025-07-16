package com.register.Services;

import de.mkammerer.argon2.Argon2;
import jakarta.inject.Singleton;

@Singleton
public class PasswordService {

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

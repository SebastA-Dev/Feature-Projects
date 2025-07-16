package com.register.Services;

import com.register.Configuration.HashConfig;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import jakarta.inject.Singleton;

@Singleton
public class PasswordHashService {

    private final HashConfig hashConfig;

    public PasswordHashService(HashConfig hashConfig) {
        this.hashConfig = hashConfig;
    }

    /**
     * Hashes a password using Argon2 algorithm.
     *
     * @param password the password to hash
     * @return the hashed password
     */
    public String hash(String password) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.valueOf(hashConfig.getType().toUpperCase()),
                hashConfig.getIterations(), hashConfig.getMemory());

        try {
            return argon2.hash(hashConfig.getIterations(), hashConfig.getMemory(), hashConfig.getParallelism(),
                    password.toCharArray());
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}

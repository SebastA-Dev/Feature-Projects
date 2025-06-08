package com.register.Services;

import com.register.Configuration.HashConfig;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import jakarta.inject.Singleton;

@Singleton
public class HashService {

    private final HashConfig hashConfig;

    public HashService(HashConfig hashConfig) {
        this.hashConfig = hashConfig;

        // Validar que el tipo sea válido
        if (hashConfig.getType() == null || hashConfig.getType().isEmpty()) {
            throw new IllegalArgumentException("Hash type must be specified in the configuration");
        }
    }

    /**
     * Hashes a password using Argon2 algorithm.
     *
     * @param password the password to hash
     * @return the hashed password
     */
    public String hash(String password) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.valueOf(hashConfig.getType()),
                hashConfig.getSaltLength(), hashConfig.getHashLength());

        try {
            return argon2.hash(hashConfig.getIterations(), hashConfig.getMemory(), hashConfig.getParallelism(),
                    password.toCharArray());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
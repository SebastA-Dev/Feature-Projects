package com.register.Services;

import com.register.Configuration.HashConfig;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestHashPasswordServices {

    @Mock
    private HashConfig hashConfig;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // -----------------------------
    // Test HashService
    // -----------------------------

    @Test
    void testHashService_validConfig_shouldHashPassword() {
        when(hashConfig.getType()).thenReturn("ARGON2id");
        when(hashConfig.getSaltLength()).thenReturn(16);
        when(hashConfig.getHashLength()).thenReturn(64);
        when(hashConfig.getIterations()).thenReturn(3);
        when(hashConfig.getMemory()).thenReturn(65536);
        when(hashConfig.getParallelism()).thenReturn(4);

        HashService hashService = new HashService(hashConfig);
        String hashed = hashService.hash("password123");

        assertNotNull(hashed);
        assertTrue(hashed.startsWith("$argon2id$"));
    }

    @Test
    void testHashService_nullType_shouldThrowException() {
        when(hashConfig.getType()).thenReturn(null);

        Executable executable = () -> new HashService(hashConfig);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, executable);
        assertEquals("Hash type must be specified in the configuration", exception.getMessage());
    }

    @Test
    void testHashService_emptyType_shouldThrowException() {
        when(hashConfig.getType()).thenReturn("");

        Executable executable = () -> new HashService(hashConfig);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, executable);
        assertEquals("Hash type must be specified in the configuration", exception.getMessage());
    }

    // -----------------------------
    // Test PasswordHashService
    // -----------------------------

    @Test
    void testPasswordHashService_invalidType_shouldThrow() {
        when(hashConfig.getType()).thenReturn("INVALID_TYPE");
        when(hashConfig.getIterations()).thenReturn(2);
        when(hashConfig.getMemory()).thenReturn(65536);
        when(hashConfig.getParallelism()).thenReturn(1);

        PasswordHashService service = new PasswordHashService(hashConfig);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            service.hash("pass");
        });

        assertTrue(ex.getMessage().contains("No enum constant"));
    }

    // -----------------------------
    // Test PasswordService
    // -----------------------------

    @Test
    void testPasswordService_verifyCorrectPassword() {
        String password = "secure123";
        Argon2 argon2 = Argon2Factory.create();
        String hash = argon2.hash(2, 65536, 1, password.toCharArray());

        PasswordService service = new PasswordService();
        assertTrue(service.verifyPassword(password, hash));
    }

    @Test
    void testPasswordService_verifyWrongPassword() {
        String password = "secure123";
        String wrongPassword = "wrongPassword";
        Argon2 argon2 = Argon2Factory.create();
        String hash = argon2.hash(2, 65536, 1, password.toCharArray());

        PasswordService service = new PasswordService();
        assertFalse(service.verifyPassword(wrongPassword, hash));
    }

    @Test
    void testPasswordService_invalidHash_shouldReturnFalse() {
        String password = "password";
        String malformedHash = "$argon2id$v=19$m=65536,t=2,p=1$INVALID";

        PasswordService service = new PasswordService();
        assertFalse(service.verifyPassword(password, malformedHash));
    }
}
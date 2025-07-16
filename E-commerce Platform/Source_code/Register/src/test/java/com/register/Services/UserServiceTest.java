package com.register.Services;

import com.register.DB.Repository.UserRepository;
import com.register.DTO.Request.UserRegistrationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private HashService hashService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_successfulRegistration_returnsTrue() {
        UserRegistrationRequest request = mock(UserRegistrationRequest.class);
        String password = "password";
        String hashedPassword = "hashedPassword";
        String ip = "127.0.0.1";

        when(request.getPassword()).thenReturn(password);
        when(hashService.hash(password)).thenReturn(hashedPassword);
        doNothing().when(userRepository).saveUser(request, hashedPassword, ip);

        Boolean result = userService.registerUser(request, ip);
        assertTrue(result);
        verify(hashService).hash(password);
        verify(userRepository).saveUser(request, hashedPassword, ip);
    }

    @Test
    void registerUser_exceptionThrown_throwsRuntimeException() {
        UserRegistrationRequest request = mock(UserRegistrationRequest.class);
        String password = "password";
        String ip = "127.0.0.1";

        when(request.getPassword()).thenReturn(password);
        when(hashService.hash(password)).thenThrow(new RuntimeException("Hash error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(request, ip);
        });
        assertTrue(exception.getMessage().contains("Error while registering user"));
        verify(hashService).hash(password);
    }

    @Test
    void getUserExistence_usernameExists_returnsTrue() {
        Map<String, String> data = new HashMap<>();
        data.put("username", "user1");
        data.put("email", "user1@email.com");

        when(userRepository.findUserByUsername("user1")).thenReturn(true);
        when(userRepository.findUserByEmail("user1@email.com")).thenReturn(false);

        boolean result = userService.getUserExistence(data);
        assertTrue(result);
        verify(userRepository).findUserByUsername("user1");
        verify(userRepository).findUserByEmail("user1@email.com");
    }

    @Test
    void getUserExistence_emailExists_returnsTrue() {
        Map<String, String> data = new HashMap<>();
        data.put("username", "user2");
        data.put("email", "user2@email.com");

        when(userRepository.findUserByUsername("user2")).thenReturn(false);
        when(userRepository.findUserByEmail("user2@email.com")).thenReturn(true);

        boolean result = userService.getUserExistence(data);
        assertTrue(result);
        verify(userRepository).findUserByUsername("user2");
        verify(userRepository).findUserByEmail("user2@email.com");
    }

    @Test
    void getUserExistence_noneExist_returnsFalse() {
        Map<String, String> data = new HashMap<>();
        data.put("username", "user3");
        data.put("email", "user3@email.com");

        when(userRepository.findUserByUsername("user3")).thenReturn(false);
        when(userRepository.findUserByEmail("user3@email.com")).thenReturn(false);

        boolean result = userService.getUserExistence(data);
        assertFalse(result);
        verify(userRepository).findUserByUsername("user3");
        verify(userRepository).findUserByEmail("user3@email.com");
    }
}

package com.register.Controllers;

import java.util.HashMap;
import java.util.Map;

import com.register.DTO.Request.UserRegistrationRequest;
import com.register.DTO.Response.RegistrationResponse;
import com.register.Services.UserService;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import jakarta.validation.Valid;

@Controller("/v1/api/")
public class UserRegistration {

    private final UserService userService;
    private String userIpAddress;
    private Map<String, Boolean> userExistenceResponse = new HashMap<>();

    public UserRegistration(UserService userService) {
        this.userService = userService;
    }

    /**
     * * Method to register a user
     * 
     * @param userRegistrationRequest the user registration request object
     * @return RegistrationResponse the registration response object
     */
    @Post(value = "/post/user-registration", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public RegistrationResponse registerUser(@Body @Valid UserRegistrationRequest userRegistrationRequest) {

        Map<String, String> fieldErrors = new HashMap<>();
        Map<String, String> userExistenceData = new HashMap<>();

        userExistenceData.put("username", userRegistrationRequest.getUsername());
        userExistenceData.put("email", userRegistrationRequest.getEmail());

        userExistenceResponse = userService.getUserExistence(userExistenceData);

        if (!userRegistrationRequest.getPassword().equals(userRegistrationRequest.getConfirmPassword())) {
            fieldErrors.put("confirmPassword", "Passwords do not match");
            return new RegistrationResponse(400, "Validation failed for the request", fieldErrors);
        }

        if (userExistenceResponse.get("username")) {
            fieldErrors.put("username", "Username already exists");
            return new RegistrationResponse(400, "Validation failed for the request", fieldErrors);
        }

        if (userExistenceResponse.get("email")) {
            fieldErrors.put("email", "Email already exists");
            return new RegistrationResponse(400, "Validation failed for the request", fieldErrors);
        }

        Boolean register = userService.registerUser(userRegistrationRequest, userIpAddress);
        if (!register) {
            fieldErrors.put("error", "User registration failed");
            return new RegistrationResponse(500, "User registration failed", fieldErrors);
        }
        return new RegistrationResponse("User registered successfully", 200);
    }

    // TODO: Implement logic for Redis (Services, repository, etc.) and then, the
    // implementation of this endpoint
    @Delete("/delete/user-registration")
    public String deleteUserRegistration(String username) {
        return "User registration deleted successfully";
    }

    // TODO: Implement logic for Redis (Services, repository, etc.) and then, the
    // implementation of this endpoint
    @Post("/post/user-registration/temporary-data")
    public String saveTemporaryData(@Body Map<String, String> temporaryData) {
        return null;
    }

}

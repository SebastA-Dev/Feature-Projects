package com.register.Endpoints;

import java.util.HashMap;
import java.util.Map;

import com.register.DTO.Request.UserRegistrationRequest;
import com.register.DTO.Response.RegistrationResponse;
import com.register.Services.UserService;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import jakarta.validation.Valid;

@Controller("/v1/api/")
public class UserRegistrationController {

    @Inject
    private UserService userService;

    private String userIpAddress;
    private Boolean userExistenceResponse = false;
    private Boolean register = false;

    /**
     * * Method to register a user
     * 
     * @param userRegistrationRequest the user registration request object
     * @return RegistrationResponse the registration response object
     */
    @Post(value = "/post/user-registration", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public RegistrationResponse registerUser(@Body @Valid UserRegistrationRequest userRegistrationRequest,
            io.micronaut.http.HttpRequest<?> request) {

        userIpAddress = request.getRemoteAddress().getHostString();

        Map<String, String> fieldErrors = new HashMap<>();
        Map<String, String> userExistenceData = new HashMap<>();

        userExistenceData.put("username", userRegistrationRequest.getUsername());
        userExistenceData.put("email", userRegistrationRequest.getEmail());

        userExistenceResponse = userService.getUserExistence(userExistenceData);

        if (userExistenceResponse) {
            return new RegistrationResponse(400, "User exists please try again",
                    Map.of("error", "User already exists"));
        }

        if (!userRegistrationRequest.getPassword().equals(userRegistrationRequest.getConfirmPassword())) {
            fieldErrors.put("confirmPassword", "Passwords do not match");
            return new RegistrationResponse(400, "Validation failed for the request", fieldErrors);
        }

        register = userService.registerUser(userRegistrationRequest, userIpAddress);
        if (!register) {
            fieldErrors.put("error", "User registration failed");
            return new RegistrationResponse(500, "User registration failed", fieldErrors);
        }
        return new RegistrationResponse("User registered successfully", 200);
    }

    // TODO: Before this implementation, we need to implement the logic for cache
    // (Dependance on Redis service)
    @Delete("/delete/user-registration")
    public String deleteUserRegistration(String username) {
        return "User registration deleted successfully";
    }

    // TODO: Before this implementation, we need to implement the logic for cache
    // (Dependance on Redis service)
    @Post("/post/user-registration/temporary-data")
    public String saveTemporaryData(@Body Map<String, String> temporaryData) {
        return null;
    }

}

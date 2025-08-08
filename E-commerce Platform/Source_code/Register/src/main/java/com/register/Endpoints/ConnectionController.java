package com.register.Endpoints;

import java.util.Dictionary;
import java.util.HashMap;

import com.register.DTO.Request.LoginRequest;
import com.register.DTO.Request.OauthRequest;
import com.register.DTO.Response.LoginResponse;
import com.register.Services.UserService;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;

@Controller("v1/application/login")
public class ConnectionController {

    @Inject
    private UserService userService;

    // TODO: complete the credentials, the user session need to bre created and the
    // return type should be the UUID of the user session, also, the testing should
    // be made
    @Post("/credentials/post")
    public LoginResponse login(@Body LoginRequest loginRequest) {
        HashMap<String, String> data = new HashMap<>();
        try {
            boolean userExistenceWithPassword = userService.checkUserExistence(loginRequest);

            if (!userExistenceWithPassword) {
                data.put("Error", "Check user credentials");
                return new LoginResponse(
                        loginRequest.getUsername() != null ? loginRequest.getUsername() : loginRequest.getEmail(),
                        401,
                        data);
            }

            userService.registerUserSession(loginRequest);
            Dictionary<String, String> userUUID = userService.getUserUUID(loginRequest);
            if (userUUID == null) {
                data.put("Error", "User UUID doesn't exist");
                return new LoginResponse(
                        loginRequest.getUsername() != null ? loginRequest.getUsername() : loginRequest.getEmail(),
                        400,
                        data);
            }

            return new LoginResponse("User authenticated succesfully", 200, 1800, userUUID.get("RefreshUUID"),
                    userUUID.get("UUID"));
        } catch (Exception e) {
            data.put("Error", "Something went wrong, user or ip address are not registered");
            return new LoginResponse(
                    loginRequest.getUsername() != null ? loginRequest.getUsername() : loginRequest.getEmail(),
                    500,
                    data);
        }

    }

    @Post("/Oauth2/UserLogin")
    public LoginResponse loginOauth2(@Body OauthRequest loginRequest) {
        return new LoginResponse(null, 0, null);
    }

}

package com.authentication;

import io.micronaut.http.annotation.*;

@Controller("/authentication")
public class AuthenticationController {

    @Get(uri="/", produces="text/plain")
    public String index() {
        return "Example Response";
    }
}
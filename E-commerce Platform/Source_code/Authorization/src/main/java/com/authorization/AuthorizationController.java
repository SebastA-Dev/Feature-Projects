package com.authorization;

import io.micronaut.http.annotation.*;

@Controller("/authorization")
public class AuthorizationController {

    @Get(uri="/", produces="text/plain")
    public String index() {
        return "Example Response";
    }
}
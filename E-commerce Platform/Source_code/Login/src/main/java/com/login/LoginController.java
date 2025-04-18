package com.login;

import io.micronaut.http.annotation.*;

@Controller("/login")
public class LoginController {

    @Get(uri="/", produces="text/plain")
    public String index() {
        return "Example Response";
    }
}

package com.register;

import io.github.cdimascio.dotenv.Dotenv;
import io.micronaut.runtime.Micronaut;

public class Application {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();

        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });

        Micronaut.run(Application.class, args);
    }
}
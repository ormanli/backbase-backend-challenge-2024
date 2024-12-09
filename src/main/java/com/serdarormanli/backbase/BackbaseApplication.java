package com.serdarormanli.backbase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
public class BackbaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackbaseApplication.class, args);
    }

}

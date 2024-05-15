package com.caito.tokeninyector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TokenInyectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(TokenInyectorApplication.class, args);
    }

}

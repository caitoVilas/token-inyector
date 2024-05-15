package com.caito.tokeninyector.api.exceptions.controllers;

import com.caito.tokeninyector.api.exceptions.customs.TokenExpirationException;
import com.caito.tokeninyector.api.models.responses.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenExpirationExceptionController {

    @ExceptionHandler(TokenExpirationException.class)
    protected ResponseEntity<ExceptionResponse> tokenExpirationHandler(TokenExpirationException e,
                                                                       HttpServletRequest request){
        var response = ExceptionResponse.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .status(HttpStatus.UNAUTHORIZED.name())
                .timestamp(LocalDateTime.now())
                .message(e.getMessage())
                .method(request.getMethod())
                .path(request.getRequestURL().toString())
                .build();
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}

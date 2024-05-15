package com.caito.tokeninyector.api.exceptions.controllers;

import com.caito.tokeninyector.api.exceptions.customs.ThreadBlockedException;
import com.caito.tokeninyector.api.models.responses.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
public class ThreadBlockedExceptionController {

    @ExceptionHandler(ThreadBlockedException.class)
    protected ResponseEntity<ExceptionResponse> threadBlockedHandler(ThreadBlockedException e,
                                                                     HttpServletRequest request) {
        var response = ExceptionResponse.builder()
                .code(HttpStatus.REQUEST_TIMEOUT.value())
                .status(HttpStatus.REQUEST_TIMEOUT.name())
                .timestamp(LocalDateTime.now())
                .message(e.getMessage())
                .method(request.getMethod())
                .path(request.getRequestURL().toString())
                .build();
        return new ResponseEntity<>(response, HttpStatus.REQUEST_TIMEOUT);
    }
}

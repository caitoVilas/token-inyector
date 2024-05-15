package com.caito.tokeninyector.api.exceptions.customs;


public class TokenExpirationException extends RuntimeException{
    public TokenExpirationException(String message) {
        super(message);
    }
}

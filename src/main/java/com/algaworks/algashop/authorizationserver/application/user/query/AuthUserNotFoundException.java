package com.algaworks.algashop.authorizationserver.application.user.query;

public class AuthUserNotFoundException extends RuntimeException {

    public AuthUserNotFoundException(String message) {
        super(message);
    }
}

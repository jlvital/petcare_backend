package com.petcare.exceptions;

public class InvalidTokenException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidTokenException() {
        super("Token inválido o expirado.");
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
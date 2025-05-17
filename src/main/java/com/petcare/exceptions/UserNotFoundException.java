package com.petcare.exceptions;

public class UserNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserNotFoundException() {
        super("Usuario no encontrado.");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
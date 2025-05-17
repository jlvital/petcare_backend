package com.petcare.exceptions;

public class IncorrectPasswordException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public IncorrectPasswordException() {
        super("Contraseña incorrecta.");
    }

    public IncorrectPasswordException(String message) {
        super(message);
    }
}
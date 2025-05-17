package com.petcare.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserAlreadyExistsException() {
        super("Ya existe un usuario registrado con ese correo.");
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
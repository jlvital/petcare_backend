package com.petcare.exceptions;

/**
 * Excepción personalizada para indicar que ya existe
 * un usuario registrado con los mismos datos (correo, username...).
 */
public class UserAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserAlreadyExistsException() {
        super("Ya existe un usuario registrado con ese correo.");
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
package com.petcare.exceptions;

/**
 * Excepción personalizada para indicar que la contraseña
 * introducida no coincide con la almacenada.
 */
public class IncorrectPasswordException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public IncorrectPasswordException() {
        super("Contraseña incorrecta.");
    }

    public IncorrectPasswordException(String message) {
        super(message);
    }
}
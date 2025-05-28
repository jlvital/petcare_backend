package com.petcare.exceptions;

/**
 * Excepción personalizada para indicar que el token proporcionado
 * es inválido, está mal formado o ha expirado.
 */
public class InvalidTokenException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidTokenException() {
        super("Token inválido o expirado.");
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
package com.petcare.exceptions;

/**
 * Excepción para errores relacionados con contraseñas:
 * login, cambio, recuperación o coincidencia.
 */
public class PasswordException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PasswordException(String message) {
        super(message);
    }
}
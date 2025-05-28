package com.petcare.exceptions;

/**
 * Excepción personalizada para indicar que las contraseñas
 * introducidas no coinciden (por ejemplo, al intentar cambiarla o registrarse).
 */
public class PasswordMismatchException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PasswordMismatchException() {
        super("Las contraseñas no coinciden.");
    }

    public PasswordMismatchException(String message) {
        super(message);
    }
}
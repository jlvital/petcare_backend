package com.petcare.exceptions;

/**
 * Excepción personalizada para indicar que el correo de recuperación del administrador
 * no está configurado correctamente en el sistema.
 * Se lanza cuando se intenta recuperar la cuenta del admin y falta el email.
 */
public class AdminRecoveryEmailException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AdminRecoveryEmailException() {
        super("El correo de recuperación del administrador no está configurado.");
    }

    public AdminRecoveryEmailException(String message) {
        super(message);
    }
}

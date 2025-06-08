package com.petcare.exceptions;

/**
 * Excepción para errores de autenticación, como credenciales incorrectas, tokens inválidos o expirados.
 */
public class AuthenticationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AuthenticationException(String message) {
        super(message);
    }
}
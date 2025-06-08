package com.petcare.exceptions;

/**
 * Excepción para denegar el acceso cuando el usuario
 * no tiene permisos suficientes o intenta acceder a datos de otros usuarios.
 */
public class UnauthorizedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UnauthorizedException(String message) {
        super(message);
    }
}
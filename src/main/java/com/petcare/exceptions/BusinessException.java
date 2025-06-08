package com.petcare.exceptions;

/**
 * Excepción para violaciones de reglas funcionales del negocio.
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BusinessException(String message) {
        super(message);
    }
}
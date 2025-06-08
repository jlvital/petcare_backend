package com.petcare.exceptions;

/**
 * Excepción para indicar que ya existe un recurso con los mismos datos.
 */

public class AlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AlreadyExistsException(String message) {
        super(message);
    }
}
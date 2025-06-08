package com.petcare.exceptions;

/**
 * Excepción para indicar que un recurso no fue encontrado:
 * usuario, cita, producto, tratamiento, etc.
 */
public class NotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NotFoundException(String message) {
        super(message);
    }
}
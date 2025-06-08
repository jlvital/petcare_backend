package com.petcare.exceptions;

/**
 * Excepción para errores durante la gestión de citas veterinarias:
 * conflictos de horario, perfil no compatible, etc.
 */
public class BookingException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BookingException(String message) {
        super(message);
    }
}
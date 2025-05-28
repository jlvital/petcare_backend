package com.petcare.exceptions;

/**
 * Excepción personalizada para errores relacionados con la gestión de citas
 * veterinarias.
 */
public class BookingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BookingException() {
		super("Error al gestionar la cita.");
	}

	public BookingException(String message) {
		super(message);
	}
}
package com.petcare.exceptions;

/**
 * Excepción personalizada para indicar que no se ha encontrado un usuario el el sistema 
 * con el ID o con los datos proporcionados (por ejemplo, el username).
 */
public class UserNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserNotFoundException() {
        super("Usuario no encontrado.");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
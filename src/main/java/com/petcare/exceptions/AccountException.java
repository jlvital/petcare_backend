package com.petcare.exceptions;

/**
 * Excepción para situaciones relacionadas con el estado de la cuenta.
 */
public class AccountException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AccountException(String message) {
        super(message);
    }
}
package com.petcare.exceptions;

/**
 * Excepción para errores relacionados con datos mal formateados,
 * inválidos o incoherentes que no encajan con la lógica esperada.
 */
public class DataException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DataException(String message) {
        super(message);
    }
}
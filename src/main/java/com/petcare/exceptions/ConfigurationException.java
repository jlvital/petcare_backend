package com.petcare.exceptions;

/**
 * Excepción para errores de configuración del sistema.
 */
public class ConfigurationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ConfigurationException(String message) {
        super(message);
    }
}
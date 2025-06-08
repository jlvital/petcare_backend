package com.petcare.utils.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Representa una respuesta estándar de error para la API REST de PetCare.
 * Se utiliza por {@code GlobalExceptionHandler} para devolver errores
 * consistentes, legibles y útiles tanto para frontend como para desarrolladores.
 *
 * <p>Incluye información técnica del error (status, tipo, mensaje)
 * y detalles adicionales opcionales como errores de validación o código interno.</p>
 *
 * @see com.petcare.exceptions.GlobalExceptionHandler
 */
@Getter
@Setter
public class ErrorResponse {

	/** Momento exacto en que ocurrió el error */
    private LocalDateTime timestamp;
    
    /** Código de estado HTTP (ej. 404, 400, 500...) */
    private int status;

    /** Código interno del sistema para identificar el tipo de error (ej. "NOT_FOUND") */
    private String errorCode;

    /** Mensaje explicativo y personalizado, orientado a mostrar en frontend */
    private String message;

    /** Lista de errores específicos, útil para validaciones de formularios */
    private List<String> errors;

    /** Ruta del endpoint donde ocurrió el error (ej. "/api/usuarios/5") */
    private String path;

    /** Descripción estándar del estado HTTP (ej. "Not Found", "Bad Request") */
    private String error;
    
}

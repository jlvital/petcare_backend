package com.petcare.domain.treatment.dto;

import com.petcare.enums.TreatmentStatus;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO usado para actualizar parcialmente un tratamiento médico.
 * Todos los campos son opcionales: solo se modificarán los que se informen.
 */
@Getter
@Setter
@NoArgsConstructor
public class TreatmentUpdate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Fecha de fin del tratamiento (opcional).
     */
    private LocalDate endDate;

    /**
     * Estado actual del tratamiento (opcional).
     */
    private TreatmentStatus status;

    /**
     * Notas clínicas adicionales (opcional).
     */
    @Size(max = 1000, message = "Las notas no pueden superar los 1000 caracteres")
    private String notes;
}
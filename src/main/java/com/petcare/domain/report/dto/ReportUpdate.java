package com.petcare.domain.report.dto;

import java.io.Serializable;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO usado para actualizar un informe médico.
 * Todos los campos son opcionales: solo se modificarán los que se informen.
 */
@Getter
@Setter
@NoArgsConstructor
public class ReportUpdate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 255, message = "El diagnóstico no puede superar los 255 caracteres")
    private String diagnosis;

    private String notes;
}
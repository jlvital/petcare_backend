package com.petcare.domain.report.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada para registrar un nuevo informe médico.
 */
@Getter
@Setter
@NoArgsConstructor
public class ReportRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 255, message = "El diagnóstico no puede superar los 255 caracteres")
    private String diagnosis;

    private String notes;

    @NotNull(message = "Debe indicarse el ID del cliente")
    private Long clientId;

    @NotNull(message = "Debe indicarse el ID de la mascota")
    private Long petId;
}
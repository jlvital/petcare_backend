package com.petcare.domain.treatment.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.petcare.enums.TreatmentStatus;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO usado para registrar un nuevo tratamiento médico.
 * Requiere la fecha de inicio, la mascota y el informe médico asociado.
 */

@Getter
@Setter
@NoArgsConstructor

public class TreatmentRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate startDate;

    @FutureOrPresent(message = "La fecha de fin no puede ser anterior a hoy")
    private LocalDate endDate;

    private TreatmentStatus status;

    private String notes;

    @NotNull(message = "Debe indicarse el ID de la mascota")
    private Long petId;

    @NotNull(message = "Debe indicarse el ID del informe médico")
    private Long medicalReportId;
}
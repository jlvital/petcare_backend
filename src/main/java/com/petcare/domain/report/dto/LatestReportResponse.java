package com.petcare.domain.report.dto;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para mostrar el resumen del último informe médico de una mascota.
 * Se utiliza en el panel principal del cliente y del empleado.
 */
@Getter
@Setter
@NoArgsConstructor
public class LatestReportResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long reportId;
    private LocalDate lastUpdate;
    private String diagnosis;
    private String notes;
    private String petName;
    private String clientName;
}
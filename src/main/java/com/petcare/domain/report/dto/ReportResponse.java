package com.petcare.domain.report.dto;

import java.time.LocalDate;

import lombok.*;

/**
 * DTO de salida para mostrar un informe médico detallado.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportResponse {

    private Long id;
    private LocalDate lastUpdate;
    private String diagnosis;
    private String notes;
}
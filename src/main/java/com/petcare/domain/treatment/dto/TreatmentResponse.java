package com.petcare.domain.treatment.dto;

import com.petcare.enums.TreatmentStatus;
import lombok.*;

import java.time.LocalDate;

/**
 * DTO de salida para representar un tratamiento.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreatmentResponse {

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;

    private TreatmentStatus status;
    private String statusLabel;

    private String notes;
}
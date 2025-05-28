package com.petcare.model.treatment.dto;

import com.petcare.enums.TreatmentStatus;
import com.petcare.model.treatment.Treatment;
import lombok.*;

import java.time.LocalDate;

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
    private String notes;

    public static TreatmentResponse fromEntity(Treatment treatment) {
        return TreatmentResponse.builder()
                .id(treatment.getId())
                .startDate(treatment.getStartDate())
                .endDate(treatment.getEndDate())
                .status(treatment.getStatus())
                .notes(treatment.getNotes())
                .build();
    }
}
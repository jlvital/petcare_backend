package com.petcare.model.report.dto;

import com.petcare.model.report.Report;
import lombok.*;

import java.time.LocalDate;

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

    public static ReportResponse fromEntity(Report report) {
        return ReportResponse.builder()
                .id(report.getId())
                .lastUpdate(report.getLastUpdate())
                .diagnosis(report.getDiagnosis())
                .notes(report.getNotes())
                .build();
    }
}
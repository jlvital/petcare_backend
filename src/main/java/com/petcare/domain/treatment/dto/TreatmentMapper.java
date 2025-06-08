package com.petcare.domain.treatment.dto;

import com.petcare.domain.pet.Pet;
import com.petcare.domain.report.Report;
import com.petcare.domain.treatment.Treatment;
import com.petcare.enums.TreatmentStatus;

/**
 * Clase auxiliar para convertir entre entidades Treatment y sus DTOs.
 */

public class TreatmentMapper {

    /**
     * Convierte un TreatmentRequest en una entidad Treatment.
     */
	
    public static Treatment toEntity(TreatmentRequest request, Pet pet, Report medicalReport) {
        if (request == null || pet == null || medicalReport == null) {
            return null;
        }

        Treatment treatment = new Treatment();
        treatment.setStartDate(request.getStartDate());
        treatment.setEndDate(request.getEndDate());
        treatment.setStatus(request.getStatus() != null ? request.getStatus() : TreatmentStatus.EN_CURSO);
        treatment.setNotes(request.getNotes());
        treatment.setPet(pet);
        treatment.setMedicalReport(medicalReport);

        return treatment;
    }

    /**
     * Aplica los cambios informados en el DTO a una entidad existente.
     */
    
    public static void updateEntityFromRequest(TreatmentUpdate request, Treatment treatment) {
        if (request == null || treatment == null) {
            return;
        }

        if (request.getEndDate() != null) {
            treatment.setEndDate(request.getEndDate());
        }

        if (request.getStatus() != null) {
            treatment.setStatus(request.getStatus());
        }

        if (request.getNotes() != null) {
            treatment.setNotes(request.getNotes());
        }
    }

    /**
     * Convierte una entidad Treatment en un DTO TreatmentResponse.
     */
    public static TreatmentResponse toResponse(Treatment treatment) {
        if (treatment == null) {
            return null;
        }

        return TreatmentResponse.builder()
                .id(treatment.getId())
                .startDate(treatment.getStartDate())
                .endDate(treatment.getEndDate())
                .status(treatment.getStatus())
                .statusLabel(treatment.getStatus() != null ? treatment.getStatus().getLabel() : null)
                .notes(treatment.getNotes())
                .build();
    }
}
package com.petcare.domain.report.dto;

import java.time.LocalDate;

import com.petcare.domain.client.Client;
import com.petcare.domain.pet.Pet;
import com.petcare.domain.report.Report;

/**
 * Clase auxiliar para convertir objetos ReportRequest en entidades Report
 * y para convertir entidades Report en distintos DTOs de respuesta.
 */

public class ReportMapper {

	 /**
     * Convierte un ReportRequest en una entidad Report.
     *
     * @param request objeto con los datos enviados desde el formulario
     * @param pet     mascota asociada al historial médico
     * @param client  cliente propietario de la mascota
     * @return entidad Report lista para guardar
     */
	
	public static Report toEntity(ReportRequest request, Pet pet) {
	    if (request == null || pet == null) {
	        return null;
	    }

        Report report = new Report();
        report.setLastUpdate(LocalDate.now());
        report.setDiagnosis(request.getDiagnosis());
        report.setNotes(request.getNotes());
        report.setPet(pet);
        return report;
    }

    /**
     * Actualiza una entidad Report con los datos informados en el DTO.
     * Solo se modificarán los campos no nulos.
     *
     * @param request objeto con los nuevos datos (pueden ser parciales)
     * @param report  informe médico existente a actualizar
     */
    
    public static void updateEntityFromRequest(ReportUpdate request, Report report) {
        if (request == null || report == null) {
            return;
        }

        if (request.getDiagnosis() != null) {
            report.setDiagnosis(request.getDiagnosis());
        }

        if (request.getNotes() != null) {
            report.setNotes(request.getNotes());
        }

        report.setLastUpdate(LocalDate.now());
    }

    public static ReportResponse toResponse(Report report) {
        if (report == null) {
            return null;
        }

        return ReportResponse.builder()
                .id(report.getId())
                .lastUpdate(report.getLastUpdate())
                .diagnosis(report.getDiagnosis())
                .notes(report.getNotes())
                .build();
    }

    /**
     * Convierte una entidad Report en un DTO de resumen LatestReportResponse.
     * Este DTO se utiliza para mostrar un resumen del último informe en el panel principal.
     *
     * @param report informe médico del que se obtiene la información
     * @return DTO con los campos más relevantes del informe
     */
    
    public static LatestReportResponse toLatestResponse(Report report) {
        if (report == null) {
            return null;
        }

        LatestReportResponse dto = new LatestReportResponse();
        dto.setReportId(report.getId());
        dto.setLastUpdate(report.getLastUpdate());
        dto.setDiagnosis(report.getDiagnosis());
        dto.setNotes(report.getNotes());

        if (report.getPet() != null) {
            dto.setPetName(report.getPet().getName());
        }

        if (report.getPet() != null && report.getPet().getClient() != null) {
            Client client = report.getPet().getClient();
            dto.setClientName(client.getName() + " " + client.getLastName1());
        }

        return dto;
    }
}
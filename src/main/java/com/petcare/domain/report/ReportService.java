package com.petcare.domain.report;

import java.util.List;

import com.petcare.domain.report.dto.LatestReportResponse;
import com.petcare.domain.report.dto.ReportRequest;
import com.petcare.domain.report.dto.ReportResponse;
import com.petcare.domain.report.dto.ReportUpdate;

public interface ReportService {
	
    List<ReportResponse> getReportsByPet(Long petId, Long clientId);
    
    /**
     * Registra un nuevo informe médico para una mascota.
     * Solo accesible para empleados autenticados.
     *
     * @param request datos del nuevo informe
     */
    void registerReport(ReportRequest request);
    
    /**
     * Actualiza parcialmente un informe médico existente.
     * Solo accesible para empleados autenticados.
     *
     * @param reportId ID del informe a modificar
     * @param request  datos actualizados
     */
    
    void updateReport(Long reportId, ReportUpdate request);
    
    /**
     * Devuelve el informe más reciente de una mascota (última actualización).
     * Solo accesible por CLIENTE o EMPLEADO.
     *
     * @param petId ID de la mascota
     * @return Último informe médico encontrado
     */
    
    LatestReportResponse getLatestReport(Long petId);
}
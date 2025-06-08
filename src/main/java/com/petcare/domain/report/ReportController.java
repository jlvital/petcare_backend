package com.petcare.domain.report;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.petcare.domain.client.Client;
import com.petcare.domain.report.dto.LatestReportResponse;
import com.petcare.domain.report.dto.ReportRequest;
import com.petcare.domain.report.dto.ReportResponse;
import com.petcare.domain.report.dto.ReportUpdate;

import java.util.List;

/**
 * Controlador REST para la gestión de informes médicos de mascotas.
 * <p>
 * Permite a los empleados registrar y actualizar informes, y a los clientes
 * consultar los informes de sus mascotas.
 * <p>
 * Incluye también una consulta mixta para recuperar el último informe disponible.
 *
 * @see ReportService
 */
@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@Slf4j
public class ReportController {

    private final ReportService reportService;

    // ╔══════════════════════════════════════════════════════════════╗
    // ║ GESTIÓN DE INFORMES MÉDICOS (REGISTRAR, EDITAR)		      ║
    // ╚══════════════════════════════════════════════════════════════╝

    /**
     * Permite a un empleado registrar un nuevo informe médico para una mascota.
     *
     * @param request Datos del informe a registrar (incluye ID de mascota y cliente).
     * @return Respuesta con estado 201 si el informe se ha registrado correctamente.
     */
    @PostMapping
    @PreAuthorize("hasRole('EMPLEADO')")
    public ResponseEntity<Void> registerReport(@RequestBody @jakarta.validation.Valid ReportRequest request) {
        log.info("Registro de nuevo informe médico para mascota ID {} (cliente ID {})",
                request.getPetId(), request.getClientId());
        reportService.registerReport(request);
        return ResponseEntity.status(201).build();
    }

    /**
     * Permite a un empleado actualizar parcialmente un informe médico existente.
     * <p>
     * Solo se modificarán los campos informados en el cuerpo de la petición.
     *
     * @param id ID del informe a actualizar.
     * @param request Campos a modificar del informe.
     * @return Respuesta sin contenido si la actualización fue exitosa.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EMPLEADO')")
    public ResponseEntity<Void> updateReport(
            @PathVariable Long id,
            @RequestBody @jakarta.validation.Valid ReportUpdate request) {

        log.info("Solicitud para actualizar el informe médico con ID: {}", id);
        reportService.updateReport(id, request);
        return ResponseEntity.noContent().build();
    }

    // ╔══════════════════════════════════════════════════════════════╗
    // ║ CONSULTAS DE INFORMES (CLIENTE Y EMPLEADO)                   ║
    // ╚══════════════════════════════════════════════════════════════╝

    /**
     * Devuelve todos los informes médicos asociados a una mascota concreta.
     * <p>
     * Solo se permiten consultas por parte del cliente propietario de la mascota.
     *
     * @param petId ID de la mascota.
     * @param client Cliente autenticado que realiza la consulta.
     * @return Lista de informes o respuesta vacía si no hay resultados.
     */
    @GetMapping("/pet/{petId}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<ReportResponse>> getReportsByPet(
            @PathVariable @NotNull Long petId,
            @AuthenticationPrincipal Client client) {

        List<ReportResponse> reports = reportService.getReportsByPet(petId, client.getId());
        return reports.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(reports);
    }

    /**
     * Devuelve el último informe médico registrado para una mascota.
     * <p>
     * Disponible tanto para clientes como para empleados.
     *
     * @param petId ID de la mascota.
     * @return Último informe médico disponible para la mascota indicada.
     */
    @GetMapping("/latest/{petId}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'EMPLEADO')")
    public ResponseEntity<LatestReportResponse> getLatestReport(@PathVariable Long petId) {
        log.info("Solicitud del último informe médico para la mascota ID: {}", petId);
        LatestReportResponse response = reportService.getLatestReport(petId);
        return ResponseEntity.ok(response);
    }
}
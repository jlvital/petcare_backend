package com.petcare.domain.treatment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.petcare.domain.client.Client;
import com.petcare.domain.treatment.dto.TreatmentRequest;
import com.petcare.domain.treatment.dto.TreatmentResponse;
import com.petcare.domain.treatment.dto.TreatmentUpdate;

import java.util.List;

/**
 * Controlador REST para la gestión de tratamientos veterinarios.
 * <p>
 * Permite a los empleados registrar y actualizar tratamientos, y a los clientes
 * consultar los tratamientos asociados a sus mascotas.
 *
 * @see TreatmentService
 * @see TreatmentRequest
 */
@RestController
@RequestMapping("/treatments")
@RequiredArgsConstructor
@Slf4j
public class TreatmentController {

	private final TreatmentService treatmentService;

	// ╔══════════════════════════════════════════════════════════════╗
	// ║ GESTIÓN DE TRATAMIENTOS (CREAR, ACTUALIZAR) 				  ║
	// ╚══════════════════════════════════════════════════════════════╝

	/**
	 * Registra un nuevo tratamiento médico para una mascota.
	 * <p>
	 * Solo los empleados pueden realizar esta operación.
	 *
	 * @param request Datos del tratamiento a registrar.
	 * @return Respuesta con estado 201 (CREATED) si se registró correctamente.
	 */
	@PostMapping
	@PreAuthorize("hasRole('EMPLEADO')")
	public ResponseEntity<Void> registerTreatment(@RequestBody @jakarta.validation.Valid TreatmentRequest request) {
		log.info("Registro de tratamiento para mascota ID {}, informe ID {}.", request.getPetId(),
				request.getMedicalReportId());
		treatmentService.registerTreatment(request);
		return ResponseEntity.status(201).build();
	}

	/**
	 * Actualiza los datos de un tratamiento existente.
	 * <p>
	 * Solo los empleados pueden realizar esta operación.
	 *
	 * @param id ID del tratamiento a modificar.
	 * @param request Nuevos datos del tratamiento.
	 * @return Respuesta vacía con estado 204 si la operación fue exitosa.
	 */
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('EMPLEADO')")
	public ResponseEntity<Void> updateTreatment(@PathVariable Long id,
			@RequestBody @jakarta.validation.Valid TreatmentUpdate request) {
		log.info("Actualización de tratamiento ID: {}", id);
		treatmentService.updateTreatment(id, request);
		return ResponseEntity.noContent().build();
	}

	// ╔══════════════════════════════════════════════════════════════╗
	// ║ CONSULTA DE TRATAMIENTOS (CLIENTE)              ║
	// ╚══════════════════════════════════════════════════════════════╝

	/**
	 * Devuelve los tratamientos asociados a una mascota específica del cliente autenticado.
	 *
	 * @param petId ID de la mascota.
	 * @param client Cliente autenticado.
	 * @return Lista de tratamientos o respuesta vacía si no hay registros.
	 */
	@GetMapping("/pet/{petId}")
	@PreAuthorize("hasRole('CLIENTE')")
	public ResponseEntity<List<TreatmentResponse>> getTreatmentsByPet(@PathVariable Long petId,
			@AuthenticationPrincipal Client client) {
		log.info("El cliente ID {} consulta los tratamientos de su mascota ID {}", client.getId(), petId);
		List<TreatmentResponse> treatments = treatmentService.getTreatmentsByPet(petId, client.getId());
		return treatments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(treatments);
	}
}
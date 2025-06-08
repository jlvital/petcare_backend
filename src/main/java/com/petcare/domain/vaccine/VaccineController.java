package com.petcare.domain.vaccine;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.petcare.domain.client.Client;
import com.petcare.domain.vaccine.dto.LatestVaccineResponse;
import com.petcare.domain.vaccine.dto.VaccineRequest;
import com.petcare.domain.vaccine.dto.VaccineResponse;
import com.petcare.domain.vaccine.dto.VaccineUpdate;

import java.util.List;

/**
 * Controlador REST para la gestión de vacunas de las mascotas.
 * <p>
 * Permite a los empleados registrar y actualizar vacunas, y a clientes y empleados
 * consultar el historial o la última vacuna aplicada a una mascota.
 *
 * @see VaccineService
 */
@RestController
@RequestMapping("/vaccines")
@RequiredArgsConstructor
@Slf4j
public class VaccineController {

	private final VaccineService vaccineService;

	// ╔════════════════════════════════════════════════════════════════════════════╗
	// ║ GESTIÓN DE VACUNAS (REGISTRAR, EDITAR)					                    ║
	// ╚════════════════════════════════════════════════════════════════════════════╝

	/**
	 * Registra una nueva vacuna aplicada a una mascota.
	 * <p>
	 * Solo accesible por usuarios con rol EMPLEADO.
	 *
	 * @param request Datos de la vacuna a registrar.
	 * @return Respuesta con estado 201 si se ha registrado correctamente.
	 */
	@PostMapping
	@PreAuthorize("hasRole('EMPLEADO')")
	public ResponseEntity<Void> registerVaccine(@RequestBody @jakarta.validation.Valid VaccineRequest request) {
		log.info("Solicitud para registrar nueva vacuna para mascota ID {}", request.getPetId());
		vaccineService.registerVaccine(request);
		return ResponseEntity.status(201).build();
	}

	/**
	 * Actualiza los datos de una vacuna existente.
	 * <p>
	 * Solo accesible por usuarios con rol EMPLEADO.
	 *
	 * @param id ID de la vacuna a modificar.
	 * @param request Datos nuevos que se desean aplicar.
	 * @return Respuesta vacía si la actualización fue exitosa.
	 */
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('EMPLEADO')")
	public ResponseEntity<Void> updateVaccine(@PathVariable Long id,
			@RequestBody @jakarta.validation.Valid VaccineUpdate request) {
		log.info("Solicitud para actualizar vacuna con ID: {}", id);
		vaccineService.updateVaccine(id, request);
		return ResponseEntity.noContent().build();
	}

	// ╔════════════════════════════════════════════════════════════════════════════╗
	// ║ CONSULTAR VACUNAS (CLIENTE / EMPLEADO)     								║
	// ╚════════════════════════════════════════════════════════════════════════════╝

	/**
	 * Devuelve todas las vacunas registradas para una mascota concreta.
	 * <p>
	 * Solo accesible por usuarios con rol CLIENTE.
	 *
	 * @param petId ID de la mascota.
	 * @param client Cliente autenticado.
	 * @return Lista de vacunas o respuesta vacía si no tiene ninguna.
	 */
	@GetMapping("/pet/{petId}")
	@PreAuthorize("hasRole('CLIENTE')")
	public ResponseEntity<List<VaccineResponse>> getVaccinesByPet(@PathVariable @NotNull Long petId,
			@AuthenticationPrincipal Client client) {

		List<VaccineResponse> vaccines = vaccineService.getVaccinesByPet(petId, client.getId());
		return vaccines.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(vaccines);
	}

	/**
	 * Devuelve la última vacuna administrada a una mascota concreta.
	 * <p>
	 * Accesible por CLIENTES y EMPLEADOS.
	 *
	 * @param petId ID de la mascota.
	 * @return Información de la última vacuna aplicada.
	 */
	@GetMapping("/latest/{petId}")
	@PreAuthorize("hasAnyRole('CLIENTE', 'EMPLEADO')")
	public ResponseEntity<LatestVaccineResponse> getLatestVaccine(@PathVariable Long petId) {
	    log.info("Solicitud de última vacuna administrada para mascota ID: {}", petId);
	    LatestVaccineResponse response = vaccineService.getLatestVaccine(petId);
	    return ResponseEntity.ok(response);
	}
}
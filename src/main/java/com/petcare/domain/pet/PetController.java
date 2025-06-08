package com.petcare.domain.pet;

import com.petcare.domain.client.Client;
import com.petcare.domain.pet.dto.PetRequest;
import com.petcare.domain.pet.dto.PetResponse;
import com.petcare.domain.pet.dto.PetUpdate;
import com.petcare.domain.user.User;
import com.petcare.validators.ClientValidator;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para operaciones relacionadas con las mascotas.
 * <p>
 * Este controlador permite al cliente:
 * <ul>
 *     <li>Registrar una nueva mascota</li>
 *     <li>Editar los datos de una mascota existente</li>
 *     <li>Consultar todas sus mascotas o una en concreto</li>
 * </ul>
 * Solo usuarios con rol CLIENTE pueden acceder a estos endpoints.
 *
 * @see PetService
 */
@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
@Slf4j
public class PetController {

	private final PetService petService;

	// ╔══════════════════════════════════════════════════════════════╗
	// ║ CLIENTE - GESTIÓN DE MASCOTAS (REGISTRAR, EDITAR) 			  ║
	// ╚══════════════════════════════════════════════════════════════╝

	/**
	 * Registra una nueva mascota para el cliente autenticado.
	 *
	 * @param request Datos de la nueva mascota.
	 * @param user Usuario autenticado (debe ser cliente).
	 * @return Mascota registrada con sus datos completos.
	 */
	@PostMapping
	public ResponseEntity<PetResponse> registerPet(@Valid @RequestBody PetRequest request,
			@AuthenticationPrincipal User user) {

		Client client = ClientValidator.validateUserIsClient(user);
		
		PetResponse response = petService.registerPet(request, client);
		log.info("Cliente ID {} registró nueva mascota con nombre '{}'", client.getId(), request.getName());
		return ResponseEntity.ok(response);
	}

	/**
	 * Actualiza los datos de una mascota del cliente autenticado.
	 *
	 * @param petId ID de la mascota a modificar.
	 * @param request Nuevos datos a aplicar.
	 * @param user Usuario autenticado (debe ser cliente).
	 * @return Mascota actualizada con la nueva información.
	 */
	@PutMapping("/{petId}")
	public ResponseEntity<PetResponse> updatePet(@PathVariable Long petId,
			@Valid @RequestBody PetUpdate request,
			@AuthenticationPrincipal User user) {

		Client client = ClientValidator.validateUserIsClient(user);
		
		PetResponse response = petService.updatePet(petId, request, client);
		log.info("Cliente ID {} actualizó la mascota ID {} (nuevo nombre: '{}')", client.getId(), petId, request.getName());
		return ResponseEntity.ok(response);
	}

	// ╔══════════════════════════════════════════════════════════════╗
	// ║ CLIENTE - CONSULTA DE MASCOTAS								  ║
	// ╚══════════════════════════════════════════════════════════════╝

	/**
	 * Devuelve todas las mascotas registradas por el cliente autenticado.
	 *
	 * @param user Usuario autenticado (debe ser cliente).
	 * @return Lista de mascotas asociadas al cliente.
	 */
	@GetMapping
	public ResponseEntity<List<PetResponse>> getClientPets(@AuthenticationPrincipal User user) {

		Client client = ClientValidator.validateUserIsClient(user);
		
		List<PetResponse> pets = petService.getPetsByClient(client.getId());
		log.info("Cliente ID {} solicitó su lista de mascotas ({} encontradas)", client.getId(), pets.size());
		return ResponseEntity.ok(pets);
	}

	/**
	 * Devuelve los datos de una mascota concreta a partir de su ID.
	 *
	 * @param petId ID de la mascota a consultar.
	 * @return Datos de la mascota encontrada.
	 */
	@GetMapping("/{petId}")
	public ResponseEntity<PetResponse> getPetById(@PathVariable Long petId) {
		PetResponse pet = petService.getPetById(petId);
		log.info("Consulta de mascota realizada para ID {}", petId);
		return ResponseEntity.ok(pet);
	}
}
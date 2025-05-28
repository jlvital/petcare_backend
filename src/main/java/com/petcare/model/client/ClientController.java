package com.petcare.model.client;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.petcare.model.client.dto.ClientUpdateRequest;
import com.petcare.model.pet.Pet;
import com.petcare.validations.ClientValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
@Slf4j
public class ClientController {

	private final ClientService clientService;

	// ╔══════════════════════════════════════════════════════════════╗
	// ║ PANEL DEL CLIENTE 											  ║
	// ╚══════════════════════════════════════════════════════════════╝
	@GetMapping("/dashboard")
	@PreAuthorize("hasRole('CLIENTE')")
	public ResponseEntity<String> getClientDashboard(@AuthenticationPrincipal Client client) {
		ClientValidator.validateAuthenticatedClient(client);
		log.info("Acceso concedido al panel del cliente ID {}", client.getId());
		return ResponseEntity.ok("Bienvenido al panel del cliente, " + client.getName());
	}

	// ╔══════════════════════════════════════════════════════════════╗
	// ║ ACTUALIZACIÓN DEL PERFIL									  ║
	// ╚══════════════════════════════════════════════════════════════╝
	@PutMapping("/profile")
	@PreAuthorize("hasRole('CLIENTE')")
	public ResponseEntity<String> updateClientProfile(@RequestBody ClientUpdateRequest request,
			@AuthenticationPrincipal Client client) {
		ClientValidator.validateAuthenticatedClient(client);
		clientService.updateClientProfile(client, request);
		log.info("Cliente ID {} actualizó su perfil correctamente", client.getId());
		return ResponseEntity.ok("Perfil actualizado correctamente.");
	}

	// ╔══════════════════════════════════════════════════════════════╗
	// ║ LISTADO DE MASCOTAS DEL CLIENTE 							  ║
	// ╚══════════════════════════════════════════════════════════════╝
	@GetMapping("/pets")
	@PreAuthorize("hasRole('CLIENTE')")
	public ResponseEntity<List<Pet>> getMyPets(@AuthenticationPrincipal Client client) {
		ClientValidator.validateAuthenticatedClient(client);

		List<Pet> pets = clientService.getPetsOfClient(client);

		if (pets.isEmpty()) {
			log.info("Cliente ID {} no tiene mascotas registradas.", client.getId());
			return ResponseEntity.noContent().build();
		}

		log.info("Cliente ID {} solicitó su lista de mascotas ({} registradas).", client.getId(), pets.size());
		return ResponseEntity.ok(pets);
	}

	// ╔══════════════════════════════════════════════════════════════╗
	// ║ REGISTRO DE NUEVA MASCOTA									  ║
	// ╚══════════════════════════════════════════════════════════════╝
	@PostMapping("/pets")
	@PreAuthorize("hasRole('CLIENTE')")
	public ResponseEntity<Pet> addPet(@RequestBody Pet pet, @AuthenticationPrincipal Client client) {
		ClientValidator.validateAuthenticatedClient(client);

		if (pet == null) {
			log.warn("Cliente ID {} envió datos de mascota nulos.", client.getId());
			return ResponseEntity.badRequest().build();
		}

		Pet savedPet = clientService.registerPet(client, pet);
		log.info("Cliente ID {} registró nueva mascota con ID {}", client.getId(), savedPet.getId());
		return ResponseEntity.ok(savedPet);
	}
}
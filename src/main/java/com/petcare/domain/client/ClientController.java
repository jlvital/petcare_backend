package com.petcare.domain.client;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.petcare.domain.client.dto.ClientMapper;
import com.petcare.domain.client.dto.ClientResponse;
import com.petcare.domain.client.dto.ClientUpdate;
import com.petcare.domain.pet.Pet;
import com.petcare.domain.user.User;
import com.petcare.utils.dto.MessageResponse;
import com.petcare.validators.ClientValidator;
import static com.petcare.utils.constants.MessageConstants.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST exclusivo para clientes.
 * <p>
 * Permite acceder al panel del cliente, editar el perfil personal y gestionar
 * las mascotas registradas (consultar, añadir...).
 * <p>
 * Todos los endpoints requieren autenticación con rol CLIENTE.
 *
 * @see ClientService
 * @see Pet
 */
@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
@Slf4j
public class ClientController {

	private final ClientService clientService;

	// ╔══════════════════════════════════════════════════════════════╗
	// ║ PANEL DE CLIENTE ║
	// ╚══════════════════════════════════════════════════════════════╝

	/**
	 * Devuelve un mensaje de bienvenida al panel del cliente autenticado.
	 *
	 * @param client Cliente autenticado.
	 * @return Saludo personalizado con el nombre del cliente.
	 */
	@GetMapping("/dashboard")
	@PreAuthorize("hasRole('CLIENTE')")
	public ResponseEntity<MessageResponse> getClientDashboard(@AuthenticationPrincipal Client client) {
		ClientValidator.validateAuthenticatedClient(client);
		log.info("Acceso concedido al panel del cliente ID {}", client.getId());
		return ResponseEntity.ok(new MessageResponse(CLIENT + client.getName()));
	}
	/**
	 * Permite al cliente actualizar sus datos personales.
	 *
	 * @param request Datos nuevos a aplicar en el perfil.
	 * @param client  Cliente autenticado.
	 * @return Mensaje indicando que la actualización fue exitosa.
	 */
	@PutMapping("/profile")
	@PreAuthorize("hasRole('CLIENTE')")
	public ResponseEntity<MessageResponse> updateClientProfile(
	        @Valid @RequestBody ClientUpdate request, @AuthenticationPrincipal User user) {

		// Validamos que el usuario autenticado es un cliente
	    Client client = ClientValidator.validateUserIsClient(user);

	    ClientValidator.validateAuthenticatedClient(client);
	    ClientValidator.validateUpdateRequest(request);

	    clientService.updateClientProfile(client, request);
	    log.info("Cliente ID {} actualizó su perfil correctamente", client.getId());
	    return ResponseEntity.ok(new MessageResponse(PROFILE_UPDATED));
	}
	
	@GetMapping("/profile")
	@PreAuthorize("hasRole('CLIENTE')")
	public ResponseEntity<ClientResponse> getClientProfile(@AuthenticationPrincipal User user) {
	    Client client = ClientValidator.validateUserIsClient(user);
	    ClientValidator.validateAuthenticatedClient(client);
	    log.info("Cliente ID {} accedió a su perfil personal", client.getId());
	    return ResponseEntity.ok(ClientMapper.toResponse(client));
	}
}
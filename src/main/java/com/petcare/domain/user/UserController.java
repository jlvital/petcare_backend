package com.petcare.domain.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.petcare.domain.user.dto.UserResponse;
import com.petcare.domain.user.dto.UserUpdate;
import com.petcare.utils.dto.MessageResponse;

/**
 * Controlador REST para la gestión del perfil del usuario autenticado.
 * <p>
 * Permite consultar y modificar los datos personales del usuario logueado, ya
 * sea cliente, empleado o administrador.
 *
 * @see UserService
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

	private final UserService userService;

	// ╔══════════════════════════════════════════════════════════════╗
	// ║ PERFIL DE USUARIO (CONSULTAR, EDITAR) ║
	// ╚══════════════════════════════════════════════════════════════╝

	/**
	 * Devuelve los datos personales del usuario autenticado.
	 * <p>
	 * El tipo de respuesta depende del tipo de usuario (cliente, empleado o usuario
	 * general).
	 *
	 * @param user Usuario autenticado a través del token JWT.
	 * @return Perfil del usuario en formato DTO.
	 */
	@GetMapping("/me")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<UserResponse> getCurrentProfile(@AuthenticationPrincipal User user) {
		UserResponse response = userService.getProfile(user);
		log.info("Perfil consultado para usuario ID {}: tipo {}", user.getId(), user.getClass().getSimpleName());
		return ResponseEntity.ok(response);
	}

	/**
	 * Actualiza parcialmente los datos del perfil del usuario autenticado.
	 * <p>
	 * Solo se modificarán los campos que se incluyan en el cuerpo de la petición.
	 *
	 * @param user    Usuario autenticado.
	 * @param request Campos que se desean modificar.
	 * @return Mensaje indicando si la actualización fue exitosa.
	 */
	@PatchMapping("/me")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<MessageResponse> updateMyProfile(@AuthenticationPrincipal User user,
	                                                       @Valid @RequestBody UserUpdate request) {
	    userService.updateUserProfile(user, request);
	    return ResponseEntity.ok(new MessageResponse("Perfil actualizado correctamente."));
	}

	@GetMapping("/paged")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Page<UserResponse>> getPagedUsers(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String role,
			@RequestParam(required = false) String status, @RequestParam(required = false) String name) {

		Page<UserResponse> response = userService.findUsersWithFilters(page, size, role, status, name);
		return ResponseEntity.ok(response);
	}
}
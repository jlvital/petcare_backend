package com.petcare.auth;

import com.petcare.auth.dto.*;
import com.petcare.domain.client.dto.ClientRequest;
import com.petcare.exceptions.*;
import com.petcare.utils.dto.MessageResponse;
import static com.petcare.utils.constants.MessageConstants.*;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST responsable de la autenticación y gestión de contraseñas.
 * <p>
 * Expone endpoints públicos para:
 * <ul>
 * <li>Registrar nuevos clientes</li>
 * <li>Iniciar sesión (login)</li>
 * <li>Cambiar o recuperar la contraseña</li>
 * <li>Reactivar cuentas bloqueadas o desactivadas</li>
 * </ul>
 * Estos endpoints permiten el acceso inicial al sistema, así como la
 * recuperación de cuentas o credenciales en caso de olvido.
 *
 * @see AuthService
 * @see com.petcare.auth.dto
 */

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

	private final AuthService authService;

	// ╔══════════════════════════════════════════════════════════════╗
	// ║ REGISTRO Y LOGIN ║
	// ╚══════════════════════════════════════════════════════════════╝

	/**
	 * Registra un nuevo cliente en el sistema.
	 * <p>
	 * El cliente debe proporcionar su nombre, apellidos, correo electrónico y una
	 * contraseña válida. Si el registro es exitoso, se genera un token JWT y se
	 * envía un correo de bienvenida.
	 *
	 * @param request Objeto con los datos del nuevo cliente.
	 * @return Respuesta con los datos del usuario y token de autenticación.
	 */

	@PostMapping("/register")
	public ResponseEntity<LoginResponse> registerUser(@Valid @RequestBody ClientRequest request) {
		log.info("Registro solicitado para nuevo cliente: {}", request.getUsername());
		LoginResponse response = authService.register(request);
		log.info("Registro exitoso para cliente: {}", request.getUsername());
		return ResponseEntity.ok(response);
	}

	/**
	 * Inicia sesión en el sistema con las credenciales proporcionadas.
	 * <p>
	 * Si el usuario existe y la contraseña es correcta, se devuelve un token JWT
	 * válido para sesiones autenticadas.
	 *
	 * @param request Objeto con el correo y la contraseña.
	 * @return Respuesta con el token de acceso y los datos del usuario.
	 */

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> authenticateUser(@Valid @RequestBody LoginRequest request) {
		log.info("Intento de login para: {}", request.getUsername());
		LoginResponse response = authService.login(request);
		log.info("Login exitoso para: {}", request.getUsername());
		return ResponseEntity.ok(response);
	}

	// ╔══════════════════════════════════════════════════════════════╗
	// ║ GESTIÓN DE CONTRASEÑA ║
	// ╚══════════════════════════════════════════════════════════════╝

	/**
	 * Permite a un usuario autenticado cambiar su contraseña.
	 * <p>
	 * La nueva contraseña debe cumplir los requisitos mínimos y coincidir con su
	 * confirmación. Esta operación requiere estar autenticado.
	 *
	 * @param dto Objeto con la nueva contraseña y su confirmación.
	 * @return Mensaje indicando que el cambio se ha realizado correctamente.
	 */

	@PostMapping("/change-password")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<MessageResponse> passwordChangeRequest(@RequestBody @Valid PasswordChangeRequest dto) {
		String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		log.info("Solicitud de cambio de contraseña para: {}", userEmail);
		authService.changePassword(userEmail, dto.getNewPassword(), dto.getConfirmPassword());
		return ResponseEntity.ok(new MessageResponse(PASSWORD_RESET_SUCCESS));
	}

	/**
	 * Solicita el envío de un enlace de recuperación de contraseña al correo
	 * registrado.
	 * <p>
	 * El sistema genera un token temporal y lo envía por email para permitir al
	 * usuario restablecer su contraseña.
	 *
	 * @param dto Objeto con el correo al que se enviará el enlace de recuperación.
	 * @return Mensaje de confirmación del envío del enlace.
	 */

	@PostMapping("/request-recovery")
	public ResponseEntity<MessageResponse> sendRecoveryLink(@RequestBody @Valid PasswordRecoveryRequest dto) {
		log.info("Solicitud de recuperación para: {}", dto.getEmail());
		authService.sendRecoveryLink(dto.getEmail());
		return ResponseEntity.ok(new MessageResponse(RECOVERY_EMAIL_SENT));
	}

	// ╔══════════════════════════════════════════════════════════════╗
	// ║ GESTIÓN DE CUENTAS ║
	// ╚══════════════════════════════════════════════════════════════╝

	/**
	 * Restablece la contraseña y reactiva la cuenta.
	 * <p>
	 * Si el token es válido y no ha expirado, se actualiza la contraseña
	 * y se reactiva automáticamente la cuenta, tanto si estaba bloqueada como desactivada.
	 *
	 * @param dto Datos del formulario con token y nueva contraseña.
	 * @return Confirmación de operación exitosa.
	 */
	@PostMapping("/recover-account")
	public ResponseEntity<MessageResponse> recoverAccount(@RequestBody @Valid PasswordResetRequest dto) {
	    log.info("Recuperación de cuenta con token: {}", dto.getToken());
	    authService.recoverAccount(dto.getToken(), dto.getNewPassword(), dto.getConfirmPassword());
	    return ResponseEntity.ok(new MessageResponse(PASSWORD_RESET_SUCCESS));
	}
}

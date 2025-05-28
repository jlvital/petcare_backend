package com.petcare.auth;

import com.petcare.auth.dto.*;
import com.petcare.exceptions.UserNotFoundException;
import com.petcare.model.client.dto.ClientRegisterRequest;
import com.petcare.utils.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    // ╔══════════════════════════════════════════════════════════════╗
    // ║                       REGISTRO Y LOGIN                       ║
    // ╚══════════════════════════════════════════════════════════════╝

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody ClientRegisterRequest request) {
        log.info("Registro solicitado para nuevo cliente con correo: {}", request.getUsername());
        AuthResponse response = authService.register(request);
        log.info("Cliente registrado exitosamente: {}", request.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody LoginRequest request) {
        log.info("Petición de inicio de sesión para: {}", request.getUsername());
        AuthResponse response = authService.login(request);
        log.info("Inicio de sesión exitoso para: {}", request.getUsername());
        return ResponseEntity.ok(response);
    }

    // ╔══════════════════════════════════════════════════════════════╗
    // ║                   CAMBIO DE CONTRASEÑA (autenticado)         ║
    // ╚══════════════════════════════════════════════════════════════╝

    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest dto, HttpServletRequest request) {
        log.info("Solicitud de cambio de contraseña para: {}", dto.getEmail());

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            log.warn("Fallo en cambio de contraseña: no coinciden los campos");
            return buildErrorResponse("Las contraseñas no coinciden.", HttpStatus.BAD_REQUEST, request);
        }

        boolean updated = authService.changeAuthUserPassword(dto.getEmail(), dto.getNewPassword());
        if (updated) {
            log.info("Contraseña actualizada correctamente para {}", dto.getEmail());
            return ResponseEntity.ok("Contraseña actualizada correctamente.");
        } else {
            log.warn("No se pudo actualizar la contraseña para {}", dto.getEmail());
            return buildErrorResponse("No se pudo actualizar la contraseña.", HttpStatus.BAD_REQUEST, request);
        }
    }

    // ╔══════════════════════════════════════════════════════════════╗
    // ║                  RECUPERACIÓN DE CONTRASEÑA                  ║
    // ╚══════════════════════════════════════════════════════════════╝

    @PostMapping("/request-password-recovery")
    public ResponseEntity<?> requestPasswordRecovery(@RequestBody @Valid RecoveryEmailRequest requestDTO, HttpServletRequest request) {
        log.info("Solicitud de recuperación de contraseña para: {}", requestDTO.getEmail());

        try {
            authService.requestPasswordRecovery(requestDTO.getEmail());
            log.info("Enlace de recuperación enviado a {}", requestDTO.getEmail());
            return ResponseEntity.ok("Hemos enviado un enlace para restablecer tu contraseña a tu correo electrónico.");
        } catch (UserNotFoundException e) {
            log.warn("Solicitud fallida: no existe cuenta con el correo {}", requestDTO.getEmail());
            return buildErrorResponse("No se encontró ninguna cuenta con ese correo.", HttpStatus.BAD_REQUEST, request);
        }
    }

    @PostMapping("/recovery-password")
    public ResponseEntity<?> recoverPassword(@RequestBody @Valid PasswordResetRequest dto, HttpServletRequest request) {
        log.info("Intentando restablecer contraseña con token: {}", dto.getToken());

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            log.warn("Restablecimiento fallido: las contraseñas no coinciden.");
            return buildErrorResponse("Las contraseñas no coinciden.", HttpStatus.BAD_REQUEST, request);
        }

        boolean success = authService.resetPasswordWithToken(dto.getToken(), dto.getNewPassword());
        if (success) {
            log.info("Contraseña restablecida correctamente con token");
            return ResponseEntity.ok("Contraseña restablecida correctamente.");
        } else {
            log.warn("Token inválido o expirado en intento de restablecimiento");
            return buildErrorResponse("Token inválido o expirado.", HttpStatus.BAD_REQUEST, request);
        }
    }

    // ╔══════════════════════════════════════════════════════════════╗
    // ║                REACTIVACIÓN DE CUENTA INACTIVA              ║
    // ╚══════════════════════════════════════════════════════════════╝

    @GetMapping("/reactivate-account")
    public ResponseEntity<?> reactivateAccount(@RequestParam String token, HttpServletRequest request) {
        log.info("Intento de reactivación de cuenta con token: {}", token);

        boolean success = authService.reactivateAccount(token);
        if (success) {
            log.info("Cuenta reactivada correctamente con token.");
            return ResponseEntity.ok("Cuenta reactivada correctamente. Ya puedes iniciar sesión.");
        } else {
            log.warn("Token inválido o expirado para reactivación.");
            return buildErrorResponse("Token inválido o expirado.", HttpStatus.BAD_REQUEST, request);
        }
    }

    // ╔══════════════════════════════════════════════════════════════╗
    // ║                      RESPUESTA DE ERRORES                    ║
    // ╚══════════════════════════════════════════════════════════════╝

    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(status.value());
        error.setError(status.getReasonPhrase());
        error.setMessage(message);
        error.setPath(request.getRequestURI());
        error.setErrorCode("ERR-" + status.value());
        return new ResponseEntity<>(error, status);
    }
}
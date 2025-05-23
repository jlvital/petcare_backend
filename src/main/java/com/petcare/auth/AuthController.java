package com.petcare.auth;

import com.petcare.auth.dto.ChangePasswordRequest;
import com.petcare.auth.dto.LoginRequest;
import com.petcare.auth.dto.PasswordResetRequest;
import com.petcare.auth.dto.RecoveryEmailRequest;
import com.petcare.auth.dto.AuthResponse;
import com.petcare.exceptions.UserNotFoundException;
import com.petcare.model.client.dto.ClientRegistrationRequest;
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

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody ClientRegistrationRequest request) {
        log.info("Intentando registrar nuevo usuario con email: {}", request.getUsername());
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody LoginRequest request) {
        log.info("Petición de login para el email: {}", request.getUsername());
        AuthResponse response = authService.login(request);
        log.info("Login exitoso para el usuario: {}", request.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest dto, HttpServletRequest request) {
        log.info("Solicitud de cambio de contraseña para: {}", dto.getEmail());

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
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

    @PostMapping("/recovery-password")
    public ResponseEntity<?> recoverPassword(@RequestBody @Valid PasswordResetRequest dto, HttpServletRequest request) {
        log.info("Intento de recuperación de contraseña con token: {}", dto.getToken());

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            return buildErrorResponse("Las contraseñas no coinciden.", HttpStatus.BAD_REQUEST, request);
        }

        boolean success = authService.resetPasswordWithToken(dto.getToken(), dto.getNewPassword());
        if (success) {
            log.info("Contraseña restablecida con éxito mediante token");
            return ResponseEntity.ok("Contraseña restablecida correctamente.");
        } else {
            log.warn("Token inválido o expirado en intento de recuperación");
            return buildErrorResponse("Token inválido o expirado.", HttpStatus.BAD_REQUEST, request);
        }
    }

    @PostMapping("/request-password-recovery")
    public ResponseEntity<?> requestPasswordRecovery(@RequestBody @Valid RecoveryEmailRequest requestDTO, HttpServletRequest request) {
        log.info("Solicitud de recuperación de contraseña para el correo: {}", requestDTO.getEmail());

        try {
            authService.requestPasswordRecovery(requestDTO.getEmail());
            return ResponseEntity.ok("Hemos enviado un enlace para restablecer tu contraseña a tu correo electrónico.");
        } catch (UserNotFoundException e) {
            log.warn("Intento con correo no registrado: {}", requestDTO.getEmail());
            return buildErrorResponse("No se encontró ninguna cuenta con ese correo.", HttpStatus.BAD_REQUEST, request);
        }
    }

    @GetMapping("/reactivate-account")
    public ResponseEntity<?> reactivateAccount(@RequestParam String token, HttpServletRequest request) {
        log.info("Intentando reactivar cuenta con token: {}", token);

        boolean success = authService.reactivateAccount(token);

        if (success) {
            return ResponseEntity.ok("Cuenta reactivada correctamente. Ya puedes iniciar sesión.");
        } else {
            return buildErrorResponse("Token inválido o expirado.", HttpStatus.BAD_REQUEST, request);
        }
    }

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
package com.petcare.validators;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.petcare.notification.SystemEmailService;
import com.petcare.enums.AccountStatus;
import com.petcare.exceptions.*;
import com.petcare.domain.user.User;
import com.petcare.domain.user.UserService;
import com.petcare.utils.constants.SecurityConstants;
import com.petcare.utils.constants.UrlConstants;
import com.petcare.auth.security.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoginValidator {

    private final SystemEmailService emailService;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public LoginValidator(SystemEmailService emailService, JwtUtil jwtUtil, UserService userService) {
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    /**
     * Este método gestiona los intentos fallidos de login.
     * Si se supera el límite permitido, la cuenta se bloquea,
     * se genera un token de recuperación y se envía un email.
     */
    
    public void onFailedLoginAttempt(User user) {
        int attempts = user.getFailedLoginAttempts() + 1;
        user.setFailedLoginAttempts(attempts);

        log.info("Intento fallido de acceso para el usuario {}. Total de intentos: {}", user.getUsername(), attempts);

        if (attempts >= SecurityConstants.MAX_LOGIN_ATTEMPTS) {
            user.setAccountStatus(AccountStatus.BLOQUEADA);
            log.warn("La cuenta del usuario {} ha sido BLOQUEADA tras demasiados intentos fallidos.", user.getUsername());

            String token = jwtUtil.generateTokenForPasswordReset(user.getUsername());
            user.setRecoveryToken(token);
            user.setRecoveryTokenExpiration(LocalDateTime.now().plusMinutes(SecurityConstants.PASSWORD_RESET_TOKEN_EXPIRATION_MINUTES));

            // Guardar el usuario actualizado (con estado BLOQUEADA y token)
            userService.saveForUserType(user);

            String recoveryEmail = user.getRecoveryEmail();
            if (recoveryEmail == null || recoveryEmail.isBlank()) {
                log.error("No se pudo enviar el email de cuenta bloqueada: recoveryEmail vacío o nulo para {}", user.getUsername());
                throw new BusinessException("No se ha podido enviar el correo de desbloqueo. Contacta con soporte.");
            }

            String recoveryLink = UrlConstants.RECOVERY_URL + token;
            emailService.sendAccountBlockedEmail(recoveryEmail, user.getName(), recoveryLink);

            log.info("Correo de recuperación enviado al usuario {}", user.getUsername());

            throw new AccountException(
                "Hemos bloqueado temporalmente tu cuenta tras varios intentos fallidos. " +
                "Te hemos enviado un correo con instrucciones para recuperarla."
            );
        }

        // Guardar también si solo aumentan los intentos (no ha llegado al máximo)
        userService.saveForUserType(user);
    }


}
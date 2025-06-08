package com.petcare.utils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.petcare.domain.user.User;
import com.petcare.domain.user.UserAccountService;
import com.petcare.domain.user.UserService;
import com.petcare.enums.AccountStatus;
import com.petcare.notification.SystemEmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.petcare.utils.constants.SecurityConstants.*;
import static com.petcare.utils.constants.GlobalConstants.*;

/**
 * Tarea programada para mantenimiento de cuentas de usuario.
 * <p>
 * Desactiva cuentas que llevan más de cierto número de días sin actividad,
 * genera un token de recuperación y notifica al usuario por email.
 * </p>
 * Se ejecuta automáticamente todos los días a las 09:00 AM.
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class MaintenanceTask {

    private final UserService userService;
    private final UserAccountService userAccountService;
    private final SystemEmailService systemEmailService;

    /**
     * Recorre los usuarios inactivos y realiza las siguientes acciones:
     * <ul>
     *   <li>Establece el estado como {@code DESACTIVADA}</li>
     *   <li>Asigna un token y una fecha de expiración</li>
     *   <li>Guarda los cambios y envía un email de notificación</li>
     * </ul>
     */
    
    @Scheduled(cron = "0 0 9 * * *") // Ejecuta todos los días a las 9:00 AM
    public void deactivateInactiveUsers() {
        List<User> inactiveUsers = userAccountService.findInactiveUsers(ACCOUNT_INACTIVITY_DAYS_LIMIT);

        for (User user : inactiveUsers) {
            user.setAccountStatus(AccountStatus.DESACTIVADA);

            String token = UUID.randomUUID().toString();
            user.setRecoveryToken(token);
            user.setRecoveryTokenExpiration(LocalDateTime.now().plusHours(PASSWORD_RESET_EXPIRATION_HOURS));

            userService.saveForUserType(user);

            systemEmailService.sendAccountDeactivationEmail(
                user.getRecoveryEmail(),
                user.getName(),
                token
            );

            log.info("Cuenta desactivada por inactividad para el usuario: {}", user.getUsername());
        }
    }
}
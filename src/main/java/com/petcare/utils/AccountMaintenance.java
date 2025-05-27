package com.petcare.utils;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.petcare.model.user.User;
import com.petcare.model.user.UserService;
import com.petcare.model.user.UserAccountService;
import com.petcare.enums.AccountStatus;
import com.petcare.email.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j

public class AccountMaintenance {

    private final UserService userService;
    private final UserAccountService userAccountService;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 9 * * *") /* Cada día a las 9:00 am */
    public void deactivateInactiveUsers() {
        List<User> inactiveUsers = userAccountService.findInactiveUsers(30);
        for (User User : inactiveUsers) {
            User.setAccountStatus(AccountStatus.DESACTIVADA);

            String token = java.util.UUID.randomUUID().toString();
            User.setRecoveryToken(token);
            User.setRecoveryTokenExpiration(java.time.LocalDateTime.now().plusHours(Constants.PASSWORD_RESET_EXPIRATION_HOURS));

            userService.saveForUserType(User);

            emailService.sendAccountDeactivationEmail(
                User.getRecoveryEmail(),
                User.getName(),
                token
            );

            log.info("Cuenta desactivada por inactividad para usuario: {}", User.getUsername());
        }
    }
}
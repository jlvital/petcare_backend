package com.petcare.admin;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.petcare.config.SystemAdmin;
import com.petcare.enums.AccountStatus;
import com.petcare.enums.Role;
import com.petcare.model.user.User;
import com.petcare.model.user.UserRepository;
import com.petcare.model.user.UserService;
import com.petcare.utils.Constants;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor

/**
 * Esta clase inicializa el administrador del sistema al arrancar la aplicación.
 * Si el admin ya está creado, no hace nada. Si no existe, lo crea con los datos de configuración.
 */

public class AdminStart {
	
    private final SystemAdmin systemAdmin;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @PostConstruct
    public void initAdmin() {
        String email = systemAdmin.getAdminEmail();
        String password = systemAdmin.getAdminPassword();

        if (email == null || password == null) {
            throw new IllegalStateException("Las propiedades del administrador no están definidas.");
        }

        if (userRepository.existsByUsername(email)) {
            log.info("El administrador {} ya existe", email);
            return;
        }

        User admin = new User();
        admin.setName(Constants.ADMIN_NAME);
        admin.setLastName1(Constants.ADMIN_LASTNAME_1);
        admin.setLastName2(Constants.ADMIN_LASTNAME_2);
        admin.setUsername(email);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setRecoveryEmail(systemAdmin.getRecoveryEmail());
        admin.setRole(Role.ADMIN);
        admin.setAccountStatus(AccountStatus.ACTIVA);

        userService.saveForUserType(admin);
        log.info("Administrador creado correctamente con email: {}", email);
    }
}
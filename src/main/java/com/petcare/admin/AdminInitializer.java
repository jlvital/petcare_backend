package com.petcare.admin;

import com.petcare.config.AdminProperties;
import com.petcare.domain.user.User;
import com.petcare.domain.user.UserRepository;
import com.petcare.domain.user.UserService;
import com.petcare.enums.AccountStatus;
import com.petcare.enums.Role;
import com.petcare.exceptions.*;
import com.petcare.utils.constants.AdminConstants;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final AdminProperties adminProperties;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    /**
     * Inicializa al administrador del sistema si no existe.
     */
    
    @PostConstruct
    public void initAdmin() {
        String email = adminProperties.getAdminEmail();
        String password = adminProperties.getAdminPassword();

        if (email == null || password == null) {
            throw new ConfigurationException("Las propiedades del administrador no están definidas en el archivo de configuración.");
        }

        Optional<User> adminOptional = userRepository.findByUsername(email);

        if (adminOptional.isEmpty()) {
            User admin = new User();
            admin.setName(AdminConstants.ADMIN_NAME);
            admin.setLastName1(AdminConstants.ADMIN_LASTNAME_1);
            admin.setLastName2(AdminConstants.ADMIN_LASTNAME_2);
            admin.setUsername(email);
            admin.setPassword(passwordEncoder.encode(password));
            admin.setRecoveryEmail(adminProperties.getRecoveryEmail());
            admin.setRole(Role.ADMIN);
            admin.setAccountStatus(AccountStatus.ACTIVA);

            userService.saveForUserType(admin);
            log.info("Administrador creado correctamente con email: {}", email);
        } else {
            User existing = adminOptional.get();
            if (existing.getRole() == Role.ADMIN) {
                log.info("El administrador {} ya existe con rol ADMIN. No es necesario crearlo.", email);
            } else {
                log.error("La cuenta de usuario {} tiene asignado un rol distinto al de Administrador. Abortando inicialización del administrador.", email);
                throw new BusinessException("La cuenta de usuario '" + email + "' ya tiene asignado el rol de '" + existing.getRole().name() + "'. No se puede crear el administrador.");
            }
        }
    }
}
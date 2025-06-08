package com.petcare.auth.security;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.petcare.config.AdminProperties;
import com.petcare.domain.user.User;
import com.petcare.domain.user.UserRepository;
import com.petcare.enums.AccountStatus;
import com.petcare.enums.Role;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j

public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AdminProperties adminProperties;

    @Value("${system.admin.email}")
    private String adminEmail;

    @Value("${system.admin.password}")
    private String adminPassword;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /*Manejo especial para el usuario administrador (predefinido)*/
        if (username.equals(adminEmail)) {
			log.info("Acceso autenticado como ADMIN desde configuración");
			return buildAdminUser();
        }

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            log.warn("Intento de login con correo no registrado: {}", username);
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        User user = userOpt.get();
        log.info("Usuario autenticado: {} con rol {}", user.getUsername(), user.getRole());

        return new CustomUserDetails(user);
    }

    /*Método auxiliar para construir el UserDetails del admin*/
    private CustomUserDetails buildAdminUser() {
        User admin = new User();
        admin.setUsername(adminProperties.getAdminEmail());
        admin.setPassword(adminProperties.getAdminPassword());
        admin.setRole(Role.ADMIN);
        admin.setName(adminProperties.getAdminName()); 
        admin.setLastName1(adminProperties.getAdminLastname1());
        admin.setLastName2(adminProperties.getAdminLastname2());  
        admin.setAccountStatus(AccountStatus.ACTIVA);
        admin.setRecoveryEmail(adminProperties.getRecoveryEmail());
        admin.setLastAccess(LocalDateTime.now());
        admin.setFailedLoginAttempts(0);

        return new CustomUserDetails(admin);
    }
}
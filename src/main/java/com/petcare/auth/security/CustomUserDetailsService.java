package com.petcare.auth.security;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.petcare.model.user.User;
import com.petcare.model.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j

public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

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
    private org.springframework.security.core.userdetails.User buildAdminUser() {
        return new org.springframework.security.core.userdetails.User(
            adminEmail,
            adminPassword,
            List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }
}
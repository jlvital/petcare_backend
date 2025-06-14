package com.petcare.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@Getter
@Setter
@Slf4j
public class AdminProperties {

    @Value("${system.admin.email}")
    private String adminEmail;

    @Value("${system.admin.password}")
    private String adminPassword;

    @Value("${system.admin.recovery.email}")
    private String recoveryEmail;

    @Value("${system.admin.name}")
    private String adminName;

    @Value("${system.admin.lastname1}")
    private String adminLastname1;

    @Value("${system.admin.lastname2}")
    private String adminLastname2;

    public boolean isAdmin(String email) {
        boolean result = this.adminEmail != null && this.adminEmail.equalsIgnoreCase(email);
        log.debug("Verificando si {} es administrador: {}", email, result);
        return result;
    }
}

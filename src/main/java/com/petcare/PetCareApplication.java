package com.petcare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableJpaAuditing
@Slf4j
@RequiredArgsConstructor
public class PetCareApplication {

    private final Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(PetCareApplication.class, args);
    }

    @PostConstruct
    public void printActiveProfiles() {
        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles.length == 0) {
            log.warn("No hay perfiles activos definidos. Usando configuración por defecto.");
        } else {
            for (String profile : activeProfiles) {
                log.info("Perfil activo: {}", profile);
            }
        }
    }
}

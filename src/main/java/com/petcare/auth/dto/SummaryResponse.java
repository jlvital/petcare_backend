package com.petcare.auth.dto;

import com.petcare.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Resumen básico de un usuario, útil para listados.
 */
@Getter
@Setter
@AllArgsConstructor
public class SummaryResponse {

    private Long id;           // ID del usuario
    private String name;       // Nombre completo
    private String username;   // Correo electrónico
    private Role role;         // Rol del usuario
    private boolean active;    // Estado de cuenta (activo/inactivo)
}
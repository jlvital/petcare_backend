package com.petcare.auth.dto;

import com.petcare.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Respuesta tras login o registro exitoso.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    
    private String token;      // JWT generado
    private Long id;           // ID del usuario autenticado
    private String name;       // Nombre del usuario
    private String username;   // Correo electrónico
    private Role role;         // Enum de rol (ADMIN, CLIENTE, EMPLEADO)
}
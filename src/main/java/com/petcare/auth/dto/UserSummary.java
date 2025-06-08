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
public class UserSummary {

	private Long id;
    private String name;
    private String username;

    private Role role;
    private String roleLabel;
    private boolean active;
}
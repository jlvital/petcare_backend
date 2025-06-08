package com.petcare.auth.dto;

import com.petcare.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Respuesta tras realizar login o registro con éxito en la aplicación.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    
	private String token;
    private Long id;
    private String name;
    private String username;

    private Role role;
    private String roleLabel;
}
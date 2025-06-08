package com.petcare.domain.user.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO usado para actualizar los datos de un usuario.
 * Todos los campos son opcionales. Solo se actualizarán aquellos que se informen (no nulos).
 */
@Getter
@Setter
@NoArgsConstructor
public class UserUpdate implements Serializable {

	private static final long serialVersionUID = 1L;
	
    @Size(min = 2, max = 30, message = "El nombre debe tener entre 2 y 30 caracteres")
    private String name;

    @Size(max = 30, message = "El primer apellido no puede superar los 30 caracteres")
    private String lastName1;

    @Size(max = 30, message = "El segundo apellido no puede superar los 30 caracteres")
    private String lastName2;

    @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "El número de teléfono debe ser válido")
    private String phoneNumber;

    @Size(max = 255, message = "La URL de la imagen no puede superar los 255 caracteres")
    private String profilePictureUrl;

    @Size(max = 100, message = "La dirección no puede superar los 100 caracteres")
    private String address;
}
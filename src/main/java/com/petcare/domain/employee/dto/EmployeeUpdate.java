package com.petcare.domain.employee.dto;

import com.petcare.enums.UserGender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO usado para actualizar los datos de un empleado. 
 * Todos los campos son opcionales. Solo se actualizarán aquellos que se informen (no nulos).
 */
@Getter
@Setter
@NoArgsConstructor
public class EmployeeUpdate implements Serializable {
	
	private static final long serialVersionUID = 1L;

    @Size(max = 50, message = "El nombre no puede superar los 50 caracteres")
    private String name;

    @Size(max = 50, message = "El primer apellido no puede superar los 50 caracteres")
    private String lastName1;

    @Size(max = 50, message = "El segundo apellido no puede superar los 50 caracteres")
    private String lastName2;

    @Email(message = "El correo de recuperación no tiene un formato válido")
    @Size(max = 100, message = "El correo de recuperación es demasiado largo")
    private String recoveryEmail;

    @Pattern(regexp = "^\\d{9}$", message = "El teléfono debe tener 9 dígitos")
    private String phoneNumber;

    @Size(max = 150, message = "La dirección es demasiado larga")
    private String address;

    private UserGender gender;
}
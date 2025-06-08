package com.petcare.domain.client.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ClientRequest {

	@NotBlank(message = "El nombre es obligatorio")
	private String name;

	/*@NotBlank(message = "El primer apellido es obligatorio")
	private String lastName1;*/

	@NotBlank(message = "El correo de acceso es obligatorio")
	@Email(message = "Formato de correo electrónico inválido")
	private String username;

	@NotBlank(message = "La contraseña es obligatoria")
	@Pattern(
	    regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*?\\-+]).{8,}$",
	    message = "Debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial")
	private String password;
}
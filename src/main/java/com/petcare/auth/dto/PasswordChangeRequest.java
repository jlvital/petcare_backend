package com.petcare.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PasswordChangeRequest {

	@NotBlank(message = "La contraseña es obligatoria")
	@Pattern(
	    regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*?\\-+]).{8,}$",
	    message = "Debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial")
    private String newPassword;

    @NotBlank(message = "La confirmación de la nueva contraseña es obligatoria")
    private String confirmPassword;
}
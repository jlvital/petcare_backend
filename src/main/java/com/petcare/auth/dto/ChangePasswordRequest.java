package com.petcare.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ChangePasswordRequest {

    @NotBlank(message = "El correo electrónico es obligatorio")
    private String email;

    @NotBlank(message = "La nueva contraseña es obligatoria")
    private String newPassword;

    @NotBlank(message = "La confirmación de la nueva contraseña es obligatoria")
    private String confirmPassword;
}
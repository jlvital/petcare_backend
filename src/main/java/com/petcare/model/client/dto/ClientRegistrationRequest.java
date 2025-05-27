package com.petcare.model.client.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientRegistrationRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El correo de acceso es obligatorio")
    @Email(message = "Formato de correo electrónico inválido")
    private String username;

    @NotBlank(message = "El correo de contacto es obligatorio")
    @Email(message = "Formato de correo de contacto inválido")
    private String recoveryEmail;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*?\\-+]).+$",
        message = "Debe tener al menos una mayúscula y un carácter especial"
    )
    private String password;
}

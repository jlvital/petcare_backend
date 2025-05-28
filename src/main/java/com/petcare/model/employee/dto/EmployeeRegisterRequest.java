package com.petcare.model.employee.dto;

import java.time.LocalDate;

import com.petcare.enums.Profile;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRegisterRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    private String lastName1;

    private String lastName2;

    //@NotBlank(message = "El correo de contacto es obligatorio")
    @Email(message = "Formato de correo de contacto inválido")
    private String recoveryEmail;

    @NotNull(message = "El perfil profesional es obligatorio")
    private Profile profile;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @PastOrPresent(message = "La fecha de inicio no puede ser futura")
    private LocalDate startDate;
}
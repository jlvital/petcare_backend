package com.petcare.domain.employee.dto;

import java.time.LocalDate;

import com.petcare.enums.Profile;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    private String lastName1;
    private String lastName2;

    @Email(message = "Introduce un correo de recuperación válido")
    private String recoveryEmail;

    @NotNull(message = "Selecciona el perfil profesional del empleado")
    private Profile profile;

    @NotNull(message = "Indica la fecha de incorporación")
    private LocalDate startDate;
}
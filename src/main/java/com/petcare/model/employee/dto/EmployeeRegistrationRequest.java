package com.petcare.model.employee.dto;

import java.time.LocalDate;

import com.petcare.enums.Profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRegistrationRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;
    private String lastName1;
    private String lastName2;

    @Email(message = "Formato de correo inválido")
    @NotBlank(message = "El correo es obligatorio")
    private String recoveryEmail;

    private Profile profile;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate startDate;
}
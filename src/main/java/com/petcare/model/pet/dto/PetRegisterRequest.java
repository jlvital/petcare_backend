package com.petcare.model.pet.dto;

import com.petcare.enums.PetGender;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PetRegisterRequest {

	@NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotNull(message = "El sexo es obligatorio")
    private PetGender petGender;

    private String chipNumber;
    private Integer age;
    private String breed;
    private LocalDate birthDate;
    private LocalDate adoptionDate;
    private Double weight;
    private Boolean sterilized = false;
    private LocalDate sterilizationDate;
    private String observations;
}
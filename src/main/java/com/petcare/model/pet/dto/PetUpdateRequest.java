package com.petcare.model.pet.dto;

import com.petcare.enums.PetGender;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PetUpdateRequest {

    private String name;
    private PetGender petGender;
    private Integer age;
    private String breed;
    private LocalDate birthDate;
    private LocalDate adoptionDate;
    private Double weight;
    private Boolean sterilized;
    private LocalDate sterilizationDate;
    private String observations;
}
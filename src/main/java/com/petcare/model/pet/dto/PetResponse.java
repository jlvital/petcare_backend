package com.petcare.model.pet.dto;

import com.petcare.enums.PetGender;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetResponse {

    private Long id;
    private String name;
    private PetGender petGender;
    private String chipNumber;
    private Integer age;
    private String breed;
    private LocalDate birthDate;
    private LocalDate adoptionDate;
    private Double weight;
    private Boolean sterilized;
    private LocalDate sterilizationDate;
    private String observations;
}
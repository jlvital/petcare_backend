package com.petcare.domain.pet.dto;

import com.petcare.enums.PetGender;
import com.petcare.enums.PetType;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;

/**
 * DTO de salida que representa los datos completos de una mascota.
 * <p>
 * Este objeto se devuelve al cliente tras registrar o consultar una mascota.
 * Incluye campos como tipo, raza, fechas, peso, observaciones, etc.
 * <p>
 * También se incluye un método {@link #getAge()} que calcula la edad actual
 * a partir de la fecha de nacimiento, si está informada.
 *
 * @see com.petcare.enums.PetType
 * @see com.petcare.enums.PetGender
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetResponse {

    private Long id;
    private String name;
    private PetGender petGender;

    private String genderLabel; 
    private String customGender;

    private PetType type;
    private String customType;

    private String chipNumber;
    private String breed;
    private LocalDate birthDate;
    private LocalDate adoptionDate;
    private Double weight;
    private Boolean sterilized;
    private LocalDate sterilizationDate;
    private String observations;

    /**
     * Este método calcula la edad en años a partir de la fecha de nacimiento.
     * Si no hay fecha, devuelve null para evitar errores.
     */
    public Integer getAge() {
        if (birthDate == null) {
            return null;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
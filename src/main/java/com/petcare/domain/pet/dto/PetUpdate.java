package com.petcare.domain.pet.dto;

import com.petcare.enums.PetGender;
import com.petcare.enums.PetType;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO usado para actualizar parcialmente los datos de una mascota.
 * <p>
 * Todos los campos son opcionales: solo se modifican aquellos que se informen (no nulos).
 * <p>
 * Se incluyen validaciones básicas como tamaños máximos o valores positivos,
 * y el sistema aplica validaciones adicionales en {@link com.petcare.validators.PetValidator}.
 * <p>
 * Este DTO es utilizado desde el perfil del cliente para editar datos como nombre, tipo, raza o esterilización.
 */

@Getter
@Setter
@NoArgsConstructor
public class PetUpdate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 50, message = "El nombre no puede superar los 50 caracteres")
    private String name;

    private PetGender petGender;
    private PetType type;

    @Size(max = 30, message = "El tipo personalizado no puede superar los 30 caracteres")
    private String customType;

    @Size(max = 30, message = "La raza no puede superar los 30 caracteres")
    private String breed;

    private LocalDate birthDate;
    private LocalDate adoptionDate;

    @DecimalMin(value = "0.0", inclusive = false, message = "El peso debe ser mayor que cero")
    private Double weight;

    private Boolean sterilized;
    private LocalDate sterilizationDate;

    @Size(max = 255, message = "Las observaciones no pueden superar los 255 caracteres")
    private String observations;
}

package com.petcare.domain.pet.dto;

import com.petcare.enums.PetGender;
import com.petcare.enums.PetType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

/**
 * DTO usado para registrar una nueva mascota desde el formulario del cliente.
 * <p>
 * Esta clase contiene todos los campos necesarios para crear una mascota,
 * junto con las validaciones que aseguran la integridad de los datos.
 * <p>
 * El campo {@code type} permite seleccionar un tipo estándar (PERRO, GATO, etc.),
 * pero si se selecciona "OTRO", es obligatorio rellenar {@code customType}.
 * <p>
 * Además, si se marca como esterilizada, también debe indicarse la fecha correspondiente.
 *
 * @see com.petcare.enums.PetType
 * @see com.petcare.enums.PetGender
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PetRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede superar los 50 caracteres")
    private String name;

    @NotNull(message = "El sexo es obligatorio")
    private PetGender petGender;
    
    @Size(max = 30, message = "El género personalizado no puede tener más de 30 caracteres")
    private String customGender;

    @NotNull(message = "El tipo es obligatorio")
    private PetType type;

    @Size(max = 30, message = "El tipo personalizado no puede superar los 30 caracteres")
    private String customType;

    @Size(max = 30, message = "El número de chip no puede superar los 30 caracteres")
    private String chipNumber;

    @Size(max = 30, message = "La raza no puede superar los 30 caracteres")
    private String breed;

    private LocalDate birthDate;
    private LocalDate adoptionDate;

    @DecimalMin(value = "0.0", inclusive = false, message = "El peso debe ser mayor que cero")
    private Double weight;

    private Boolean sterilized = false;
    private LocalDate sterilizationDate;

    @Size(max = 255, message = "Las observaciones no pueden superar los 255 caracteres")
    private String observations;
}
package com.petcare.domain.vaccine.dto;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para mostrar el resumen de la última vacuna administrada.
 * Se utiliza en el panel del cliente y del empleado.
 */
@Getter
@Setter
@NoArgsConstructor
public class LatestVaccineResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long vaccineId;
    private String name;
    private String lab;
    private LocalDate administrationDate;
    private LocalDate expirationDate;
    private String petName;
}
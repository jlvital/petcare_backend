package com.petcare.domain.vaccine.dto;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO usado para actualizar parcialmente una vacuna.
 * Todos los campos son opcionales: solo se modificarán los que se informen.
 */
@Getter
@Setter
@NoArgsConstructor
public class VaccineUpdate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String name;

    @Size(max = 100, message = "El laboratorio no puede superar los 100 caracteres")
    private String lab;

    private LocalDate administrationDate;

    private LocalDate expirationDate;

    @Positive(message = "El precio de compra debe ser mayor que 0")
    private Double purchasePrice;

    @Positive(message = "El precio de venta debe ser mayor que 0")
    private Double salePrice;
}
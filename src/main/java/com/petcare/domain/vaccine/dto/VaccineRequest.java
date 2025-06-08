package com.petcare.domain.vaccine.dto;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class VaccineRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "El nombre de la vacuna es obligatorio")
    private String name;

    @NotBlank(message = "El laboratorio es obligatorio")
    private String lab;

    @NotNull(message = "La fecha de administración es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate administrationDate;

    private LocalDate expirationDate;

    @NotNull(message = "El precio de compra debe estar informado")
    @Positive(message = "El precio de compra debe ser mayor que 0")
    private Double purchasePrice;

    @NotNull(message = "El precio de venta debe estar informado")
    @Positive(message = "El precio de venta debe ser mayor que 0")
    private Double salePrice;

    @NotNull(message = "Debe indicarse el ID de la mascota")
    private Long petId;
}
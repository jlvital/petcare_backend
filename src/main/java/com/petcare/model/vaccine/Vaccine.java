package com.petcare.model.vaccine;

import java.time.LocalDate;

import com.petcare.config.Auditable;
import com.petcare.model.pet.Pet;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vaccine")

public class Vaccine extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVaccine;
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El fabricante no puede estar vacío")
    private String lab;

    @NotNull(message = "La fecha de administración debe estar informada")
    @PastOrPresent(message = "La fecha de administración no puede ser futura")
    private LocalDate administrationDate;

    private LocalDate expirationDate;

    @NotNull(message = "El precio de compra debe estar informado")
    @Positive(message = "El precio de compra debe ser mayor a 0")
    private Double purchasePrice;

    @NotNull(message = "El precio de venta debe estar informado")
    @Positive(message = "El precio de venta debe ser mayor a 0")
    private Double salePrice;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;
}
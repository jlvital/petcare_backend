package com.petcare.domain.vaccine;

import java.time.LocalDate;

import com.petcare.config.Auditable;
import com.petcare.domain.pet.Pet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vacunas")
public class Vaccine extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vacuna")
    private Long idVaccine;

    @Column(name = "nombre")
    private String name;

    @Column(name = "laboratorio")
    private String lab;

    @Column(name = "fecha_administracion")
    private LocalDate administrationDate;

    @Column(name = "fecha_caducidad")
    private LocalDate expirationDate;

    @Column(name = "precio_compra")
    private Double purchasePrice;

    @Column(name = "precio_venta")
    private Double salePrice;

    @ManyToOne
    @JoinColumn(name = "id_mascota")
    private Pet pet;
}
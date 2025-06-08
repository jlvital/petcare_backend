package com.petcare.domain.report;

import java.time.LocalDate;

import com.petcare.config.Auditable;
import com.petcare.domain.pet.Pet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
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
@Table(name = "informes_medicos")
public class Report extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_informe")
    private Long id;

    @Column(name = "fecha_actualizacion")
    private LocalDate lastUpdate = LocalDate.now();

    @Column(name = "diagnostico")
    private String diagnosis;

    @Lob
    @Column(name = "notas")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "id_mascota")
    private Pet pet;
}
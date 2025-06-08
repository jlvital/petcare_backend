package com.petcare.domain.treatment;

import java.time.LocalDate;

import com.petcare.config.Auditable;
import com.petcare.domain.pet.Pet;
import com.petcare.domain.report.Report;
import com.petcare.enums.TreatmentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "tratamientos")
public class Treatment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tratamiento")
    private Long id;

    @Column(name = "fecha_inicio")
    private LocalDate startDate = LocalDate.now();

    @Column(name = "fecha_fin")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private TreatmentStatus status = TreatmentStatus.EN_CURSO;

    @Lob
    @Column(name = "notas")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "id_mascota")
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "id_informe", nullable = false)
    private Report medicalReport;
}
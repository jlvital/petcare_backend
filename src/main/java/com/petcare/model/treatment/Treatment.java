package com.petcare.model.treatment;

import java.time.LocalDate;

import com.petcare.config.Auditable;
import com.petcare.enums.TreatmentStatus;
import com.petcare.model.report.Report;
import com.petcare.model.pet.Pet;

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
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "treatment")

public class Treatment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La fecha de inicio del tratamiento es obligatoria")
    private LocalDate startDate = LocalDate.now();

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private TreatmentStatus status = TreatmentStatus.EN_CURSO;

    @Lob
    private String notes;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "medical_report_id", nullable = false)
    private Report medicalReport;
}
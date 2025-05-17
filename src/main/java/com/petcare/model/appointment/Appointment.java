package com.petcare.model.appointment;

import java.time.LocalDate;
import java.time.LocalTime;

import com.petcare.config.Auditable;
import com.petcare.enums.AppointmentStatus;
import com.petcare.enums.AppointmentType;
import com.petcare.model.employee.Employee;
import com.petcare.model.pet.Pet;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "citas")
public class Appointment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha", nullable = false)
    @NotNull(message = "La fecha es obligatoria")
    private LocalDate date;

    @Column(name = "hora", nullable = false)
    @NotNull(message = "La hora es obligatoria")
    private LocalTime time;

    @Column(name = "recordatorios", nullable = false)
    private Boolean reminderSent = false;

    @Column(name = "estado", nullable = false)
    @NotNull(message = "El estado de la cita es obligatorio")
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @Column(name = "tipo", nullable = false)
    @NotNull(message = "El tipo de cita es obligatorio")
    @Enumerated(EnumType.STRING)
    private AppointmentType type;

    @ManyToOne
    @JoinColumn(name = "mascota_id")
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "empleado_id", nullable = false)
    private Employee employee;
}
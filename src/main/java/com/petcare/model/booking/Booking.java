package com.petcare.model.booking;

import java.time.LocalDate;
import java.time.LocalTime;

import com.petcare.config.Auditable;
import com.petcare.enums.BookingStatus;
import com.petcare.enums.BookingType;

import com.petcare.model.employee.Employee;
import com.petcare.model.pet.Pet;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "citas")
public class Booking extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha", nullable = false)
    @NotNull(message = "La fecha de la cita es obligatoria")
    @FutureOrPresent(message = "La fecha no puede ser anterior a la actual")
    private LocalDate date;

    @Column(name = "hora", nullable = false)
    @NotNull(message = "Indica la hora de la cita")
    private LocalTime time;

    @Column(name = "recordatorio", nullable = false)
    private Boolean reminderRequest = false;
    
    @Column(name = "notificado", nullable = false)
    private Boolean reminderSent = false;

    @Column(name = "estado", nullable = false)
    @NotNull(message = "El estado de la cita es obligatorio")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(name = "tipo", nullable = false)
    @NotNull(message = "Selecciona qué tipo de cita quieres agendar")
    @Enumerated(EnumType.STRING)
    private BookingType type;

    @ManyToOne
    @JoinColumn(name = "id_mascota", nullable = false)
    @NotNull(message = "Selecciona la mascota para la que quieres agendar la cita")
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "id_empleado", nullable = false)
    @NotNull(message = "Selecciona el nombre de uno de nuestros empleados")
    private Employee employee;
}
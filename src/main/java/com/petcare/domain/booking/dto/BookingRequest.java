package com.petcare.domain.booking.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.petcare.enums.BookingType;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO utilizado para registrar una nueva cita en el sistema.
 * <p>
 * Incluye todos los datos obligatorios que el cliente debe completar para solicitar una cita.
 * Todas las propiedades están validadas mediante anotaciones JSR-303.
 */
@Getter
@Setter
@NoArgsConstructor
public class BookingRequest {

    /** Fecha solicitada para la cita (no puede ser en el pasado). */
    @NotNull(message = "La fecha de la cita es obligatoria")
    @FutureOrPresent(message = "La fecha no puede ser anterior a la actual")
    private LocalDate date;

    /** Hora deseada para la cita. */
    @NotNull(message = "Indica la hora de la cita")
    private LocalTime time;

    /** Tipo de cita a realizar (vacuna, revisión, etc.). */
    @NotNull(message = "Selecciona qué tipo de cita quieres agendar")
    private BookingType type;

    /** Indica si el cliente desea recibir un recordatorio por email. */
    @NotNull(message = "Indica si deseas recibir un recordatorio")
    private Boolean reminderRequest;

    /** ID de la mascota a la que corresponde la cita. */
    @NotNull(message = "Selecciona la mascota para la que quieres agendar la cita")
    private Long petId;

    /** ID del empleado que atenderá la cita. */
    @NotNull(message = "Selecciona el nombre de uno de nuestros empleados")
    private Long employeeId;
}
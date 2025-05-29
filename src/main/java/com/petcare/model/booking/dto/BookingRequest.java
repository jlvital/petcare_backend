package com.petcare.model.booking.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.petcare.enums.BookingType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookingRequest {

    @NotNull(message = "La fecha de la cita es obligatoria")
    private LocalDate date;

    @NotNull(message = "La hora de la cita es obligatoria")
    private LocalTime time;

    @NotNull(message = "El tipo de cita es obligatorio")
    private BookingType type;

    @NotNull(message = "Debe seleccionar una mascota")
    private Long petId;

    @NotNull(message = "Debe seleccionar un empleado")
    private Long employeeId;

    @NotNull(message = "Debe indicar si desea recibir un recordatorio")
    private Boolean reminderRequest;
}
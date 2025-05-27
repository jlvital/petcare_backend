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
public class BookingUpdateRequest {

    @NotNull(message = "Debe indicar una nueva fecha")
    private LocalDate newDate;

    @NotNull(message = "Debe indicar una nueva hora")
    private LocalTime newTime;

    @NotNull(message = "Debe seleccionar un nuevo tipo de cita")
    private BookingType newType;

    @NotNull(message = "Debe seleccionar un empleado")
    private Long newEmployeeId;

    @NotNull(message = "Debe indicar si desea recibir un recordatorio")
    private Boolean reminderRequested;
}
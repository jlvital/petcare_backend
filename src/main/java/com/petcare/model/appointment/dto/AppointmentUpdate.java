package com.petcare.model.appointment.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.petcare.enums.AppointmentType;
import com.petcare.enums.NotificationType;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AppointmentUpdate {

    @NotNull(message = "Debe indicar una nueva fecha")
    private LocalDate newDate;

    @NotNull(message = "Debe indicar una nueva hora")
    private LocalTime newTime;

    @NotNull(message = "Debe seleccionar un nuevo tipo de cita")
    private AppointmentType newType;

    @NotNull(message = "Debe seleccionar un nuevo profesional disponible")
    private Long newEmployeeId;

    @NotNull(message = "Debe indicar si desea recibir un recordatorio")
    private Boolean reminderRequested;

    private NotificationType reminderChannel = NotificationType.EMAIL;
}

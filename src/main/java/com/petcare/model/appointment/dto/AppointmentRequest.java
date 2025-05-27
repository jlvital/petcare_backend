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
public class AppointmentRequest {

    @NotNull(message = "La fecha de la cita es obligatoria")
    private LocalDate date;

    @NotNull(message = "La hora de la cita es obligatoria")
    private LocalTime time;

    @NotNull(message = "El tipo de cita es obligatorio")
    private AppointmentType type;

    @NotNull(message = "Debe seleccionar una mascota")
    private Long petId;

    @NotNull(message = "Debe seleccionar un profesional disponible")
    private Long employeeId;

    @NotNull(message = "Debe indicar si desea recibir un recordatorio")
    private Boolean reminderRequested;

    // Por defecto se asigna EMAIL si no se especifica
    private NotificationType reminderChannel = NotificationType.EMAIL;
}

package com.petcare.model.appointment.dto;

import com.petcare.enums.AppointmentStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class AppointmentStatusRequest {
	@NotNull(message = "El ID de la cita es obligatorio")
    private Long appointmentId;

	@NotNull(message = "El nuevo estado de la cita es obligatorio")
    private AppointmentStatus newStatus;
}


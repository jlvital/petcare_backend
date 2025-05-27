package com.petcare.model.booking.dto;

import com.petcare.enums.BookingStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class BookingStatusRequest {
	@NotNull(message = "El ID de la cita es obligatorio")
    private Long bookingId;

	@NotNull(message = "El nuevo estado de la cita es obligatorio")
    private BookingStatus newStatus;
}
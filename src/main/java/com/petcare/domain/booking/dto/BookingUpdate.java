package com.petcare.domain.booking.dto;

import com.petcare.enums.BookingType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO usado para actualizar los datos de una cita existente.
 * <p>
 * Todos los campos son opcionales. Solo se actualizarán aquellos que no sean nulos.
 * Se emplea en conjunto con validaciones personalizadas en el servicio.
 */
@Getter
@Setter
@NoArgsConstructor
public class BookingUpdate implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Nueva fecha para la cita (si se desea modificar). */
    private LocalDate newDate;

    /** Nueva hora para la cita (si se desea modificar). */
    private LocalTime newTime;

    /** Nuevo tipo de cita (si se desea modificar). */
    private BookingType newType;

    /** Nueva solicitud de recordatorio (si se desea modificar). */
    private Boolean reminderRequest;

    /** Nuevo ID de profesional que atenderá la cita (si se cambia). */
    private Long newEmployeeId;
}
package com.petcare.validators;

import com.petcare.domain.booking.Booking;
import com.petcare.enums.BookingStatus;
import com.petcare.exceptions.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BookingValidatorRules {

    /**
     * Valida si una cita puede pasar a un nuevo estado.
     */
    public static void validateStatus(Booking booking, BookingStatus newStatus) {
        if (booking == null || newStatus == null) {
            throw new DataException("Faltan datos para actualizar el estado de la cita.");
        }

        BookingStatus currentStatus = booking.getStatus();

        if (currentStatus == BookingStatus.COMPLETADA) {
            throw new BookingException("Esta cita ya fue completada y no puede modificarse.");
        }

        if (currentStatus == BookingStatus.CANCELADA && newStatus == BookingStatus.CANCELADA) {
            throw new BookingException("Esta cita ya fue cancelada por el cliente.");
        }

        if (currentStatus == BookingStatus.ANULADA && newStatus == BookingStatus.ANULADA) {
            throw new BookingException("Esta cita ya ha sido anulada.");
        }

        if (currentStatus == newStatus) {
            throw new BookingException("El estado de la cita no se ha modificado.");
        }

        log.info("La cita ha pasado de [{}] a [{}]", currentStatus, newStatus);
    }

    /**
     * Valida si un cliente tiene derecho a modificar una cita.
     */
    public static void validateClientOwnership(Booking booking, Long clientId) {
        if (booking == null || booking.getPet() == null || booking.getPet().getClient() == null) {
            throw new DataException("No se puede verificar a quién corresponde la cita. Faltan datos del cliente.");
        }

        if (!booking.getPet().getClient().getId().equals(clientId)) {
            throw new BookingException("No tienes permisos para modificar esta cita. Solo el propietario puede hacerlo.");
        }

        log.info("Validación de propiedad correcta: Cliente ID [{}] puede modificar cita ID [{}]",
                clientId, booking.getId());
    }
}
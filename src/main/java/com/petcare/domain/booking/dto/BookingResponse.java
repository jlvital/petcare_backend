package com.petcare.domain.booking.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.petcare.enums.BookingStatus;
import com.petcare.enums.BookingType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO de respuesta que contiene todos los datos necesarios para mostrar una cita al cliente o empleado.
 * <p>
 * Incluye información enriquecida como nombres, etiquetas y duración estándar.
 * Está diseñado para ser consumido directamente por el frontend.
 */
@Getter
@Setter
@Builder
public class BookingResponse {

    /** ID único de la cita. */
    private Long id;

    /** Fecha programada para la cita. */
    private LocalDate date;

    /** Hora programada para la cita. */
    private LocalTime time;

    /** Estado actual de la cita (CONFIRMADA, CANCELADA, etc.). */
    private BookingStatus status;

    /** Texto legible del estado (por ejemplo: "Completada"). */
    private String statusLabel;

    /** Tipo de cita (vacuna, revisión, etc.). */
    private BookingType type;

    /** Texto legible del tipo de cita. */
    private String typeLabel;

    /** Si el cliente ha solicitado recordatorio. */
    private Boolean reminderRequest;

    /** Si el recordatorio ya ha sido enviado. */
    private Boolean reminderSent;

    /** ID de la mascota relacionada. */
    private Long petId;

    /** Nombre de la mascota. */
    private String petName;

    /** Tipo de mascota (PERRO, GATO o personalizado si es OTRO). */
    private String petType;

    /** ID del empleado asignado a la cita. */
    private Long employeeId;

    /** Nombre completo del empleado. */
    private String employeeName;

    /** Perfil profesional del empleado (VETERINARIO, PELUQUERO, etc.). */
    private String employeeProfile;
    
    /** Texto legible del perfil del empleado */ 
    private String employeeProfileLabel;

    /** Nombre del cliente que solicitó la cita. */
    private String clientName;

    /** Duración estándar de la cita en minutos. */
    private Integer durationMinutes;
}
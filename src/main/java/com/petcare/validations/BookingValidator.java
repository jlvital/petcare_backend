package com.petcare.validations;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.petcare.enums.BookingType;
import com.petcare.enums.Profile;
import com.petcare.model.booking.BookingRepository;
import com.petcare.model.client.Client;

public class BookingValidator {

    private static final Logger log = LoggerFactory.getLogger(BookingValidator.class);

    // ═══════════════════════════════════════════════════════════════
    // VALIDACIÓN: Perfil adecuado según tipo de cita
    // ═══════════════════════════════════════════════════════════════
    public static void validateEmployeeProfile(BookingType type, Profile profile) {
        if (type == null) {
            log.warn("Tipo de cita no especificado. No se puede validar el perfil requerido.");
            throw new IllegalArgumentException("Debes seleccionar un tipo de cita.");
        }

        if (profile == null) {
            log.warn("Profesional sin perfil asignado. No se puede validar su compatibilidad.");
            throw new IllegalArgumentException("El profesional no tiene perfil asignado.");
        }

        if (!type.getRequiredProfile().equals(profile)) {
            log.warn("Perfil incompatible: se requiere [{}] para citas tipo [{}], pero el profesional tiene [{}].",
                    type.getRequiredProfile(), type, profile);
            throw new IllegalArgumentException("El profesional no puede atender este tipo de cita.");
        }

        log.info("Perfil validado correctamente: [{}] puede atender citas de tipo [{}].", profile, type);
    }

    // ═══════════════════════════════════════════════════════════════
    // VALIDACIÓN: Disponibilidad del profesional
    // ═══════════════════════════════════════════════════════════════
    public static void validateAvailability(BookingRepository bookingRepository, Long employeeId, LocalDate date, LocalTime time) {
        if (bookingRepository.existsByEmployeeIdAndDateAndTime(employeeId, date, time)) {
            log.warn("El profesional [{}] ya tiene una cita asignada el {} a las {}.", employeeId, date, time);
            throw new IllegalArgumentException("El profesional ya tiene una cita en esa fecha y hora.");
        }

        log.info("El profesional [{}] está disponible el {} a las {}.", employeeId, date, time);
    }

    // ═══════════════════════════════════════════════════════════════
    // VALIDACIÓN: Recordatorios (solo email)
    // ═══════════════════════════════════════════════════════════════
    public static void validateReminderSettings(Boolean reminderRequest, Client client) {
        if (Boolean.TRUE.equals(reminderRequest)) {
            if (client == null || client.getRecoveryEmail() == null || client.getRecoveryEmail().trim().isEmpty()) {
                log.warn("Recordatorio solicitado pero el cliente no tiene email de recuperación configurado.");
                throw new IllegalArgumentException("Debes tener un correo registrado para recibir recordatorios.");
            }
            log.info("Cliente ID [{}] recibirá recordatorio por email a: {}", client.getId(), client.getRecoveryEmail());
        } else {
            log.info("El cliente ha decidido no recibir recordatorio.");
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // VALIDACIÓN: Fecha y hora válidas (horarios laborales)
    // ═══════════════════════════════════════════════════════════════
    public static void validateDateAndTime(LocalDate date, LocalTime time) {
        LocalDate today = LocalDate.now();

        if (date == null || time == null) {
            log.warn("Faltan datos para validar fecha y hora de la cita.");
            throw new IllegalArgumentException("Debes indicar la fecha y la hora de la cita.");
        }

        if (date.isBefore(today)) {
            log.warn("Fecha no válida: {} es anterior al día actual.", date);
            throw new IllegalArgumentException("No puedes agendar una cita en el pasado.");
        }

        DayOfWeek day = date.getDayOfWeek();
        int hour = time.getHour();
        int minute = time.getMinute();

        boolean isWeekday = day.getValue() >= 1 && day.getValue() <= 5;
        boolean isSaturday = day == DayOfWeek.SATURDAY;

        boolean validWeekdayTime = (hour >= 10 && hour < 14) || (hour >= 17 && hour < 20);
        boolean validSaturdayTime = hour >= 10 && hour < 14;
        boolean isHalfHour = minute == 0 || minute == 30;

        if ((isWeekday && !validWeekdayTime) || (isSaturday && !validSaturdayTime) || (!isHalfHour)) {
            log.warn("Horario inválido: {} {} no cumple las condiciones laborales ni el formato de 30 minutos.", date, time);
            throw new IllegalArgumentException("La cita debe ser en horario laboral y comenzar en punto o y media.");
        }

        log.info("Fecha y hora validadas correctamente: {} {}", date, time);
    }
}
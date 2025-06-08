package com.petcare.validators;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import com.petcare.enums.BookingType;
import com.petcare.enums.Profile;
import com.petcare.exceptions.BookingException;
import com.petcare.exceptions.DataException;
import com.petcare.domain.booking.BookingRepository;
import com.petcare.domain.client.Client;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BookingValidator {

    // ═══════════════════════════════════════════════════════════════
    // VALIDACIÓN: Perfil adecuado según tipo de cita
    // ═══════════════════════════════════════════════════════════════
    public static void validateEmployeeProfile(BookingType type, Profile profile) {
        if (type == null) {
            log.warn("Tipo de cita no especificado. No se puede validar el perfil requerido.");
            throw new BookingException("Debes seleccionar un tipo de cita.");
        }

        if (profile == null) {
            log.warn("Empleado sin perfil asignado. No se puede validar su compatibilidad.");
            throw new BookingException("El empleado seleccionado no tiene perfil asignado.");
        }

        if (!type.isCompatibleWith(profile)) {
            log.warn("Perfil incompatible: se requiere un perfil [{}] para citas de tipo [{}] y el empleado tiene perfil [{}].",
                    type.getRequiredProfile(), type, profile);
            throw new BookingException("El empleado seleccionado no puede atender este tipo de cita.");
        }

        log.info("Perfil validado correctamente: [{}] puede atender citas de tipo [{}].", profile, type);
    }

    // ═══════════════════════════════════════════════════════════════
    // VALIDACIÓN: Disponibilidad del profesional
    // ═══════════════════════════════════════════════════════════════
    public static void validateAvailability(BookingRepository bookingRepository, Long employeeId, LocalDate date, LocalTime time) {
        if (bookingRepository.existsByEmployeeIdAndDateAndTime(employeeId, date, time)) {
            log.warn("El profesional [{}] ya tiene una cita asignada el {} a las {}.", employeeId, date, time);
            throw new BookingException("El empleado seleccionado ya tiene una cita en esa fecha y hora.");
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
                throw new DataException("Debes tener un correo registrado para recibir recordatorios.");
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
            throw new DataException("Debes indicar la fecha y la hora de la cita.");
        }

        if (date.isBefore(today)) {
            log.warn("Fecha no válida: {} es anterior al día actual.", date);
            throw new BookingException("No puedes agendar una cita en una fecha anterior a la de hoy.");
        }

        if (date.equals(today)) {
            LocalTime now = LocalTime.now();
            if (time.isBefore(now.plusMinutes(60))) {
                log.warn("Hora no válida: {} está a menos de 60 minutos desde ahora ({})", time, now);
                throw new BookingException("Debes agendar la cita con al menos 60 minutos de antelación.");
            }
        }

        DayOfWeek day = date.getDayOfWeek();
        int hour = time.getHour();
        int minute = time.getMinute();

        boolean isWeekday = day.getValue() >= 1 && day.getValue() <= 5;
        boolean isSaturday = day == DayOfWeek.SATURDAY;

        boolean validWeekdayTime = (hour >= 10 && hour < 14) || (hour >= 17 && hour < 20);
        boolean validSaturdayTime = hour >= 10 && hour < 14;
        boolean isQuarterHour = minute == 0 || minute == 15 || minute == 30 || minute == 45;

        if ((isWeekday && !validWeekdayTime) || (isSaturday && !validSaturdayTime) || !isQuarterHour) {
            log.warn("Horario inválido: {} {} no cumple las condiciones laborales ni el formato permitido.", date, time);
            throw new BookingException("Debes agendar la cita dentro de nuestro horario laboral.");
        }

        log.info("Fecha y hora validadas correctamente: {} {}", date, time);
    }
}
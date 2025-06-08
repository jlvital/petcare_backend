package com.petcare.utils;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.petcare.domain.booking.Booking;
import com.petcare.domain.booking.BookingRepository;
import com.petcare.enums.BookingStatus;
import com.petcare.notification.BookingEmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.petcare.utils.constants.EmailConstants.*;
import static com.petcare.utils.constants.GlobalConstants.*;


/**
 * Tarea programada que envía recordatorios por correo electrónico para las 
 * citas confirmadas que tienen prevista su celebración el día siguiente.
 * <p>
 * Se ejecuta automáticamente todos los días a las 10:00 AM.
 * </p>
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class ReminderTask {

    private final BookingRepository bookingRepository;
    private final BookingEmailService bookingEmailService;

    /**
     * Busca las citas confirmadas para el día siguiente que han solicitado recordatorio,
     * y envía un correo electrónico personalizado al cliente.
     */
    
    @Scheduled(cron = "0 0 10 * * *") // Executes every day at 10:00 AM
    public void sendReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Booking> bookings = bookingRepository.findByDate(tomorrow);

        for (Booking booking : bookings) {
            boolean reminderRequest = Boolean.TRUE.equals(booking.getReminderRequest());
            boolean reminderNotSent = Boolean.FALSE.equals(booking.getReminderSent());
            boolean isConfirmed = BookingStatus.CONFIRMADA.equals(booking.getStatus());

            if (reminderRequest && reminderNotSent && isConfirmed) {
                String recipientEmail = booking.getPet().getClient().getUsername();
                String clientName = booking.getPet().getClient().getName();
                String petName = booking.getPet().getName();
                String formattedDate = booking.getDate().format(DATE_FORMATTER);
                String formattedTime = booking.getTime().format(TIME_FORMATTER);

                bookingEmailService.sendBookingReminder(
                    recipientEmail,
                    clientName,
                    petName,
                    formattedDate,
                    formattedTime,
                    DEFAULT_LOCATION
                );

                booking.setReminderSent(true);
                bookingRepository.save(booking);

                log.info("Reminder email sent to client {}", recipientEmail);
            }
        }
    }
}
package com.petcare.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.petcare.model.booking.Booking;
import com.petcare.model.booking.BookingRepository;
import com.petcare.email.EmailBookingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingReminder {

    private final BookingRepository bookingRepository;
    private final EmailBookingService emailBookingService;

    private static final String DEFAULT_LOCATION = "PetCare Clínica Veterinaria";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Envía recordatorios de citas confirmadas que ocurren mañana
     * y que han solicitado notificación por email.
     */
    @Scheduled(cron = "0 0 10 * * *") // Todos los días a las 10:00
    public void sendReminders() {
        LocalDate targetDate = LocalDate.now().plusDays(1);
        List<Booking> bookings = bookingRepository.findByDate(targetDate);

        for (Booking booking : bookings) {
            boolean debeRecordar = Boolean.TRUE.equals(booking.getReminderRequest());
            boolean noEnviado = Boolean.FALSE.equals(booking.getReminderSent());
            boolean estaConfirmada = "CONFIRMADA".equals(booking.getStatus().name());

            if (debeRecordar && noEnviado && estaConfirmada) {
                String to = booking.getPet().getClient().getUsername(); // email del cliente

                emailBookingService.sendBookingReminder(
                    to,
                    booking.getPet().getClient().getName(),             // name
                    booking.getPet().getName(),                         // petName
                    booking.getDate().format(DATE_FORMAT),             // date
                    booking.getTime().format(TIME_FORMAT),             // time
                    DEFAULT_LOCATION                                   // location
                );

                booking.setReminderSent(true);
                bookingRepository.save(booking);

                log.info("📩 Recordatorio de cita enviado por EMAIL al cliente {}", to);
            }
        }
    }
}
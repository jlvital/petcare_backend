package com.petcare.email;

import com.petcare.enums.EmailTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Implementación del servicio de notificaciones de correo para la gestión de citas.
 * Renderiza las plantillas HTML y delega el envío en EmailService.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailBookingServiceImpl implements EmailBookingService {

    private final EmailTemplateEngine emailTemplate;
    private final EmailService emailService;

    @Override
    public void sendBookingReminder(String recipientEmail, String name, String petName, String date, String time, String location) {
        try {
            Map<String, Object> variables = Map.of(
                "name", name,
                "petName", petName,
                "date", date,
                "time", time,
                "location", location
            );
            String content = emailTemplate.render(EmailTemplate.BOOKING_REMINDER, variables);
            emailService.sendHtmlEmail(recipientEmail, EmailTemplate.BOOKING_REMINDER.getSubject(), content);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el recordatorio de cita", e);
        }
    }

    @Override
    public void sendBookingConfirmation(String recipientEmail, String clientName, String date, String time,
                                        String employeeName, String type, String status) {
        try {
            Map<String, Object> variables = Map.of(
                "clientName", clientName,
                "date", date,
                "time", time,
                "employeeName", employeeName,
                "type", type,
                "status", status
            );
            String content = emailTemplate.render(EmailTemplate.BOOKING_CONFIRMATION, variables);
            emailService.sendHtmlEmail(recipientEmail, EmailTemplate.BOOKING_CONFIRMATION.getSubject(), content);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar la confirmación de cita al cliente", e);
        }
    }
    
    @Override
    public void sendBookingAssigned(String recipientEmail, String clientName, String petName,
                                    String date, String time, String type) {
        try {
            Map<String, Object> variables = Map.of(
                "clientName", clientName,
                "petName", petName,
                "date", date,
                "time", time,
                "type", type
            );
            String content = emailTemplate.render(EmailTemplate.BOOKING_ASSIGNED, variables);
            emailService.sendHtmlEmail(recipientEmail, EmailTemplate.BOOKING_ASSIGNED.getSubject(), content);
        } catch (Exception e) {
            throw new RuntimeException("Error al notificar la asignación de cita al empleado", e);
        }
    }

    @Override
    public void sendBookingInfoUpdate(String recipientEmail, String clientName, String petName, String oldDate, String date,
                                      String time, String employeeName, String type) {
        try {
            Map<String, Object> variables = Map.of(
                "clientName", clientName,
                "petName", petName,
                "oldDate", oldDate,
                "date", date,
                "time", time,
                "employeeName", employeeName,
                "type", type
            );
            String content = emailTemplate.render(EmailTemplate.BOOKING_INFO, variables);
            emailService.sendHtmlEmail(recipientEmail, EmailTemplate.BOOKING_INFO.getSubject(), content);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar notificación de cita modificada al empleado", e);
        }
    }

    @Override
    public void sendBookingCancelled(String recipientEmail, String clientName, String petName, String date, String time,
                                     String employeeName, String type) {
        try {
            Map<String, Object> variables = Map.of(
                "clientName", clientName,
                "petName", petName,
                "date", date,
                "time", time,
                "employeeName", employeeName,
                "type", type
            );
            String content = emailTemplate.render(EmailTemplate.BOOKING_CANCELLED, variables);
            emailService.sendHtmlEmail(recipientEmail, EmailTemplate.BOOKING_CANCELLED.getSubject(), content);
        } catch (Exception e) {
            throw new RuntimeException("Error al notificar la cancelación al empleado", e);
        }
    }

    @Override
    public void sendBookingAborted(String recipientEmail, String clientName, String employeeName, String date, String time,
                                   String rescheduleLink) {
        try {
            Map<String, Object> variables = Map.of(
                "clientName", clientName,
                "employeeName", employeeName,
                "date", date,
                "time", time,
                "rescheduleLink", rescheduleLink
            );
            String content = emailTemplate.render(EmailTemplate.BOOKING_ABORTED, variables);
            emailService.sendHtmlEmail(recipientEmail, EmailTemplate.BOOKING_ABORTED.getSubject(), content);
        } catch (Exception e) {
            throw new RuntimeException("Error al informar al cliente de la anulación de cita", e);
        }
    }
}
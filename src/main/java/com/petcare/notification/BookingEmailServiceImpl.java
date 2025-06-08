package com.petcare.notification;

import com.petcare.enums.EmailTemplate;
import com.petcare.utils.constants.*;
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
public class BookingEmailServiceImpl implements BookingEmailService {

    private final TemplateRenderer emailTemplate;
    private final SystemEmailService systemEmailService;

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
            systemEmailService.sendHtmlEmail(recipientEmail, EmailTemplate.BOOKING_REMINDER.getSubject(), content);
        } catch (Exception e) {
            throw new RuntimeException(EmailConstants.SEND_BOOKING_REMINDER_ERROR, e);
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
            systemEmailService.sendHtmlEmail(recipientEmail, EmailTemplate.BOOKING_CONFIRMATION.getSubject(), content);
        } catch (Exception e) {
            throw new RuntimeException(EmailConstants.SEND_BOOKING_CONFIRMATION_ERROR, e);
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
            systemEmailService.sendHtmlEmail(recipientEmail, EmailTemplate.BOOKING_ASSIGNED.getSubject(), content);
        } catch (Exception e) {
            throw new RuntimeException(EmailConstants.SEND_BOOKING_ASSIGNED_ERROR, e);
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
            systemEmailService.sendHtmlEmail(recipientEmail, EmailTemplate.BOOKING_INFO.getSubject(), content);
        } catch (Exception e) {
            throw new RuntimeException(EmailConstants.SEND_BOOKING_UPDATE_ERROR, e);
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
            systemEmailService.sendHtmlEmail(recipientEmail, EmailTemplate.BOOKING_CANCELLED.getSubject(), content);
        } catch (Exception e) {
            throw new RuntimeException(EmailConstants.SEND_BOOKING_CANCELLED_ERROR, e);
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
            systemEmailService.sendHtmlEmail(recipientEmail, EmailTemplate.BOOKING_ABORTED.getSubject(), content);
        } catch (Exception e) {
            throw new RuntimeException(EmailConstants.SEND_BOOKING_ABORTED_ERROR, e);
        }
    }
}
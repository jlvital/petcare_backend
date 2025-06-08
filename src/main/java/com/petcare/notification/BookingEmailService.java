package com.petcare.notification;

/**
 * Interfaz especializada para el envío de correos relacionados con la gestión
 * de citas.
 */
public interface BookingEmailService {

	void sendBookingReminder(String recipientEmail, String name, String petName, String date, String time,
			String location);

	void sendBookingConfirmation(String recipientEmail, String clientName, String date, String time,
			String employeeName, String type, String status);

	void sendBookingInfoUpdate(String recipientEmail, String clientName, String petName, String oldDate, String date,
			String time, String employeeName, String type);

	void sendBookingCancelled(String recipientEmail, String clientName, String petName, String date, String time,
			String employeeName, String type);

	void sendBookingAborted(String recipientEmail, String clientName, String employeeName, String date, String time,
			String rescheduleLink);

	void sendBookingAssigned(String recipientEmail, String clientName, String petName, String date, String time,
			String type);
}
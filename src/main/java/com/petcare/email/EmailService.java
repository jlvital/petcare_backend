package com.petcare.email;

import jakarta.annotation.Nullable;

public interface EmailService {

	void sendWelcomeEmail(String recipientEmail, String role, String displayName, @Nullable String password);

	void sendPasswordRecoveryEmail(String recipientEmail, String token);

	void sendAccountDeactivationEmail(String recipientEmail, String displayName, String token);

	void sendAppointmentReminder(String recipientEmail, String name, String petName, String date, String time,
								 String veterinarian, String location);

	void sendHtmlEmail(String to, String subject, String htmlBody);
}
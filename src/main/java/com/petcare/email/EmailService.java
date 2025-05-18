package com.petcare.email;

public interface EmailService {

	public void sendWelcomeEmail(String recipientEmail, String role, String displayName, String username, String password);
	
	void sendWelcomeEmail(String recipientEmail, String role, String displayName, String password);

	void sendPasswordRecoveryEmail(String recipientEmail, String token);

	void sendAccountDeactivationEmail(String recipientEmail, String displayName, String token);

	void sendAppointmentReminder(String recipientEmail, String name, String petName, String date, String time,
								 String veterinarian, String location);

	void sendHtmlEmail(String to, String subject, String htmlBody);
}
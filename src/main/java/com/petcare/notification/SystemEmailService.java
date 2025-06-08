package com.petcare.notification;

/**
 * Interfaz general para el envío de correos electrónicos del sistema PetCare.
 * Incluye bienvenida, recuperación, desactivación y envío de HTML renderizado.
 */
public interface SystemEmailService {

	/** Enviar email de bienvenida al usuario (versión con username y contraseña). */
	void sendWelcomeEmail(String recipientEmail, String role, String displayName, String username, String password);

	/** Enviar email de bienvenida al usuario (solo nombre visible y contraseña). */
	void sendWelcomeEmail(String recipientEmail, String role, String displayName, String password);

	/** Enviar email de bienvenida al empleado con enlace seguro para cambiar su contraseña. */
	void sendWelcomeEmail(String recipientEmail, String role, String displayName, String username, String password,
			String resetLink);

	/** Enviar email de recuperación de contraseña. */
	public void sendPasswordRecoveryEmail(String recipientEmail, String displayName, String recoveryLink);

	/** Enviar email por desactivación de cuenta. */
	void sendAccountDeactivationEmail(String recipientEmail, String displayName, String token);

	/** Enviar email cuando una cuenta ha sido bloqueada por seguridad. */
	void sendAccountBlockedEmail(String recipientEmail, String displayName, String recoveryLink);

	/** Enviar cualquier email HTML ya renderizado. */
	void sendHtmlEmail(String to, String subject, String htmlBody);
}
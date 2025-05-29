package com.petcare.enums;

/**
 * Enum que representa las plantillas de correo disponibles en PetCare. Cada
 * plantilla se asocia a un archivo HTML y a un asunto (subject) del correo.
 */
public enum EmailTemplate {

	WELCOME_CLIENT("welcome_client", "¡Bienvenido a PetCare!"),
	WELCOME_EMPLOYEE("welcome_employee", "Acceso al sistema - PetCare"),
	PASSWORD_RECOVERY("password_recovery", "Recuperación de contraseña - PetCare"),
	ACCOUNT_DEACTIVATION("account_deactivation", "Cuenta desactivada por inactividad"),
	ACCOUNT_BLOCKED("account_blocked", "Cuenta bloqueada temporalmente"),

	BOOKING_CONFIRMATION("booking_confirmation", "Cita confirmada - PetCare"),
	BOOKING_INFO("booking_info", "Cita modificada - PetCare"),
	BOOKING_REMINDER("booking_reminder", "Recordatorio de cita - PetCare"),
	BOOKING_CANCELLED("booking_cancelled", "Cita cancelada por cliente"),
	BOOKING_ABORTED("booking_aborted", "Cita anulada por la clínica"),
	BOOKING_ASSIGNED("booking_assigned", "Nueva cita asignada - PetCare");

	private final String fileName;
	private final String subject;

	EmailTemplate(String fileName, String subject) {
		this.fileName = fileName;
		this.subject = subject;
	}

	public String getFileName() {
		return fileName;
	}

	public String getSubject() {
		return subject;
	}
}
package com.petcare.enums;

/**
 * Enum que representa las plantillas de correo disponibles en PetCare.
 * Cada plantilla se asocia a un archivo HTML y a un asunto (subject) del correo.
 */
public enum EmailTemplate {

    // --- Correos de bienvenida ---
    WELCOME_CLIENT("welcome_client", "¡Bienvenido a PetCare!"),
    WELCOME_EMPLOYEE("welcome_employee", "Bienvenido al equipo de PetCare 🐾"),

    // --- Recuperación de cuenta / estado de cuenta ---
    PASSWORD_RECOVERY("password_recovery", "Recupera tu contraseña 🔒"),
    ACCOUNT_DEACTIVATION("account_deactivation", "Cuenta desactivada por inactividad"),

    // --- Gestión de citas: cliente ---
    BOOKING_CONFIRMATION("booking_confirmation", "Cita confirmada - PetCare"),
    BOOKING_REMINDER("booking_reminder", "Recordatorio de tu cita - PetCare"),
    BOOKING_CANCELLED("booking_cancelled", "Cita cancelada por cliente"),

    // --- Gestión de citas: empleado / clínica ---
    BOOKING_INFO("booking_info", "Cita modificada - PetCare"),
    BOOKING_ABORTED("booking_aborted", "Cita anulada por la clínica"),
    BOOKING_ASSIGNED("booking_assigned", "Nueva cita asignada - PetCare");

    private final String fileName; // Nombre del archivo HTML (sin extensión)
    private final String subject;  // Asunto del correo

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

package com.petcare.enums;

/**
 * Enum que representa las distintas plantillas de correo electrónico utilizadas en PetCare.
 * <p>
 * Cada valor del enum está vinculado a:
 * <ul>
 *     <li>Un archivo HTML (ubicado en <code>src/main/resources/templates/email</code>)</li>
 *     <li>Un asunto de correo (<i>subject</i>) personalizado para cada tipo de mensaje</li>
 * </ul>
 * Estas plantillas se usan para enviar notificaciones transaccionales como confirmaciones de citas,
 * recordatorios, recuperación de contraseña o información de bienvenida.
 * <p>
 * Este enfoque centraliza la gestión de nombres de archivo y asuntos, facilitando su mantenimiento.
 */
public enum EmailTemplate {

    /** Correo de bienvenida enviado tras el registro de un nuevo cliente */
    WELCOME_CLIENT("client", "¡Bienvenido a PetCare!"),

    /** Correo enviado a un nuevo empleado con sus credenciales de acceso */
    WELCOME_EMPLOYEE("employee", "Acceso al sistema - PetCare"),

    /** Correo con enlace para recuperar la contraseña de acceso */
    PASSWORD_RECOVERY("recovery", "Recuperación de contraseña - PetCare"),

    /** Correo que informa al usuario de que su cuenta ha sido desactivada */
    ACCOUNT_DEACTIVATION("deactivated", "Cuenta desactivada por inactividad"),

    /** Correo que notifica un bloqueo temporal de la cuenta */
    ACCOUNT_BLOCKED("blocked", "Cuenta bloqueada temporalmente"),

    /** Confirmación de una nueva cita por parte del cliente */
    BOOKING_CONFIRMATION("confirmed", "Cita confirmada - PetCare"),

    /** Información sobre cambios realizados en una cita */
    BOOKING_INFO("details", "Cita modificada - PetCare"),

    /** Recordatorio enviado al cliente antes de su cita */
    BOOKING_REMINDER("reminder", "Recordatorio de cita - PetCare"),

    /** Notificación de cita cancelada por el cliente */
    BOOKING_CANCELLED("cancelled", "Cita cancelada por cliente"),

    /** Notificación de cita anulada por el personal de la clínica */
    BOOKING_ABORTED("aborted", "Cita anulada por la clínica"),

    /** Notificación enviada al empleado cuando se le asigna una nueva cita */
    BOOKING_ASSIGNED("assigned", "Nueva cita asignada - PetCare");

    private final String fileName;
    private final String subject;

    EmailTemplate(String fileName, String subject) {
        this.fileName = fileName;
        this.subject = subject;
    }

    /**
     * Devuelve el nombre del archivo de plantilla asociado.
     * <p>
     * Este nombre se corresponde con un archivo HTML en la carpeta de plantillas.
     *
     * @return Nombre del archivo de plantilla sin la extensión (.html)
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Devuelve el asunto del correo asociado a esta plantilla.
     *
     * @return Asunto personalizado del mensaje de correo.
     */
    public String getSubject() {
        return subject;
    }
}
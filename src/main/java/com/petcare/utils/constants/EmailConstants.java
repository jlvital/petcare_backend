package com.petcare.utils.constants;

/**
 * Constantes utilizadas para la configuración del envío de correos electrónicos,
 * rutas de plantillas HTML y formato de fechas en notificaciones.
 */

public final class EmailConstants {

    /**
     * Constructor privado para evitar la instanciación de esta clase de constantes.
     */
    private EmailConstants() { }

    // ╔══════════════════════════════════════╗
    // ║ Configuración general email          ║
    // ╚══════════════════════════════════════╝

    public static final String FROM_ADDRESS = "notificaciones.petcare@gmail.com";
    public static final String TEMPLATE_FOLDER = "templates/email/";
    public static final String TEMPLATE_SUFFIX = ".html";
    public static final String TEMPLATE_MODE = "HTML";
    public static final String LOGO_IMAGE_PATH = "classpath:templates/email/logo.png";
    public static final String LOGO_CID = "logo";

    // ╔══════════════════════════════════════╗
    // ║ Ubicación        					  ║
    // ╚══════════════════════════════════════╝

    public static final String DEFAULT_LOCATION = "PetCare Clínica Veterinaria";

    // ╔══════════════════════════════════════╗
    // ║ Errores generales                    ║
    // ╚══════════════════════════════════════╝

    public static final String SEND_WELCOME_ERROR = "Error al enviar el email de bienvenida.";
    public static final String SEND_RECOVERY_ERROR = "Error al enviar el email de recuperación de contraseña.";
    public static final String SEND_BLOCKED_ERROR = "Error al enviar el email de bloqueo de cuenta.";
    public static final String SEND_DEACTIVATION_ERROR = "Error al enviar el email de desactivación por inactividad.";
    public static final String SEND_HTML_ERROR = "Error al enviar el correo electrónico con formato HTML.";
    public static final String LOAD_STYLESHEET_ERROR = "No se pudo cargar la hoja de estilos comunes.";
    public static final String ROLE_NOT_SUPPORTED_ERROR = "Rol no soportado: ";

    // ╔══════════════════════════════════════╗
    // ║ Errores relacionados con citas       ║
    // ╚══════════════════════════════════════╝

    public static final String SEND_BOOKING_REMINDER_ERROR = "Error al enviar el recordatorio de cita.";
    public static final String SEND_BOOKING_CONFIRMATION_ERROR = "Error al enviar la confirmación de cita al cliente.";
    public static final String SEND_BOOKING_ASSIGNED_ERROR = "Error al notificar la asignación de cita al empleado.";
    public static final String SEND_BOOKING_UPDATE_ERROR = "Error al enviar notificación de cita modificada al empleado.";
    public static final String SEND_BOOKING_CANCELLED_ERROR = "Error al notificar la cancelación al empleado.";
    public static final String SEND_BOOKING_ABORTED_ERROR = "Error al informar al cliente de la anulación de cita.";
}
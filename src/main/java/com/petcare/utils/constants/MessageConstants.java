package com.petcare.utils.constants;

/**
 * Mensajes utilizados en la interacción con el usuario.
 * Se aplican en validaciones, errores y respuestas HTTP estructuradas.
 * Organizados por secciones funcionales para facilitar su reutilización.
 */
public final class MessageConstants {

    /**
     * Constructor privado para evitar la instanciación de esta clase de constantes.
     */
    private MessageConstants() {}

    // ╔══════════════════════════════════════╗
    // ║ Autenticación y cuenta               ║
    // ╚══════════════════════════════════════╝

    public static final String BLOCKED = "Tu cuenta está bloqueada. Revisa tu correo para desbloquearla.";
    public static final String REACTIVATED = "Cuenta reactivada correctamente. Ya puedes iniciar sesión.";
    public static final String INVALID = "El enlace de recuperación es inválido o ha expirado.";
    public static final String PASSWORD_RESET_SUCCESS = "Contraseña restablecida correctamente.";
    public static final String PASSWORD_UPDATE_FAIL = "No se pudo actualizar la contraseña. Inténtalo de nuevo más tarde.";
    public static final String RECOVERY_EMAIL_SENT = "Hemos enviado un enlace de recuperación a tu correo electrónico.";

    // ╔══════════════════════════════════════╗
    // ║ Paneles de bienvenida                ║
    // ╚══════════════════════════════════════╝

    public static final String ADMIN = "Bienvenido al panel de administrador";
    public static final String CLIENT = "Bienvenid@ a tu área privda, ";
    public static final String EMPLOYEE = "Bienvenid@ a tu panel del empleado, ";

    // ╔══════════════════════════════════════╗
    // ║ Confirmaciones de perfil              ║
    // ╚══════════════════════════════════════╝

    public static final String PROFILE_UPDATED = "Perfil actualizado correctamente.";

    // ╔══════════════════════════════════════╗
    // ║ Citas (booking)                      ║
    // ╚══════════════════════════════════════╝

    public static final String CANCELLED = "Cita cancelada correctamente.";
    public static final String ABORTED = "Cita anulada correctamente.";
    public static final String COMPLETED = "Cita marcada como completada.";

    // ╔══════════════════════════════════════╗
    // ║ Eliminación de usuarios              ║
    // ╚══════════════════════════════════════╝

    public static final String USER_DELETED = "El usuario ha sido definitivamente eliminado del sistema.";

    // ╔══════════════════════════════════════╗
    // ║ Errores genéricos			          ║
    // ╚══════════════════════════════════════╝

    public static final String VALIDATION_ERROR = "Error de validación";
    public static final String SERVER_ERROR = "Error interno del servidor";
    public static final String ACCESS_DENIED = "No tienes permisos para realizar esta acción.";
    public static final String RESOURCE_NOT_FOUND = "No se encontró el recurso solicitado.";
    public static final String OPERATION_NOT_ALLOWED = "Esta operación no está permitida.";
    public static final String UNEXPECTED_ERROR = "Ha ocurrido un error inesperado. Inténtalo más tarde.";
    public static final String OPERATION_SUCCESS = "Operación realizada correctamente.";
}
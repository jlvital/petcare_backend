package com.petcare.enums;

/**
 * Enum que representa los distintos roles de usuario en el sistema PetCare.
 * <p>
 * Cada rol determina los permisos, vistas y acciones disponibles en la aplicación.
 * También se utiliza para controlar el acceso a los endpoints mediante anotaciones de seguridad.
 * <p>
 * Los roles disponibles son:
 * <ul>
 *     <li>{@code CLIENTE} – Usuario que puede registrar mascotas, agendar citas y realizar compras.</li>
 *     <li>{@code EMPLEADO} – Profesional veterinario o auxiliar que atiende citas y gestiona el sistema.</li>
 *     <li>{@code ADMIN} – Usuario con control total del sistema, incluyendo usuarios y reportes.</li>
 * </ul>
 * <p>
 * Cada valor incluye una etiqueta legible que se muestra en la interfaz de usuario.
 */
public enum Role implements Labeled {

    CLIENTE("Cliente"),
    EMPLEADO("Empleado"),
    ADMIN("Administrador");

    /** Texto descriptivo del rol, utilizado en la interfaz. */
    private final String label;

    Role(String label) {
        this.label = label;
    }

    /**
     * Devuelve la etiqueta legible asociada al rol.
     *
     * @return Texto visible como "Cliente", "Empleado" o "Administrador".
     */
    @Override
    public String getLabel() {
        return label;
    }

    /** Rol por defecto que se asigna a los usuarios registrados desde el frontend. */
    public static final Role DEFAULT = CLIENTE;
}
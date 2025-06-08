package com.petcare.enums;

/**
 * Enum que representa el género de un usuario del sistema.
 * <p>
 * Se utiliza durante el registro de clientes y empleados, principalmente con fines estadísticos
 * o para personalizar el trato en la interfaz (por ejemplo, mensajes, saludos o formularios).
 * <p>
 * Los valores definidos actualmente son:
 * <ul>
 *     <li>{@code HOMBRE} – Masculino</li>
 *     <li>{@code MUJER} – Femenino</li>
 *     <li>{@code OTRO} – Género no binario o preferencia personalizada</li>
 * </ul>
 * Cada opción incluye una etiqueta legible que se muestra al usuario.
 */
public enum UserGender implements Labeled {

    HOMBRE("Hombre"),
    MUJER("Mujer"),
    OTRO("Otro");

    /** Texto visible asociado al género para mostrar en la interfaz. */
    private final String label;

    UserGender(String label) {
        this.label = label;
    }

    /**
     * Devuelve la etiqueta legible del género.
     *
     * @return Texto como "Hombre", "Mujer" u "Otro".
     */
    @Override
    public String getLabel() {
        return label;
    }
}
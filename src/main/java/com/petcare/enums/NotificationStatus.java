package com.petcare.enums;

/**
 * Enum que representa el estado de las notificaciones por parte de un usuario.
 * <p>
 * Este valor se puede usar para controlar si un cliente desea recibir notificaciones
 * (como recordatorios de citas) por correo electrónico.
 */
public enum NotificationStatus implements Labeled {

    ACTIVADAS("Activadas"),
    DESACTIVADAS("Desactivadas");

    /** Texto descriptivo que representa el estado de notificación. */
    private final String label;

    NotificationStatus(String label) {
        this.label = label;
    }

    /**
     * Devuelve la etiqueta legible del estado.
     * @return Texto visible para el usuario (ej. "Activadas").
     */
    @Override
    public String getLabel() {
        return label;
    }

    /** Estado predeterminado al registrar un nuevo usuario. */
    public static final NotificationStatus DEFAULT = DESACTIVADAS;
}
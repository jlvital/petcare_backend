package com.petcare.enums;

/**
 * Interfaz funcional utilizada por enumeraciones que deben proporcionar
 * un nombre legible o "etiqueta" para mostrar al usuario.
 * <p>
 * Esta interfaz es implementada por enums como {@link BookingStatus}, {@link BookingType},
 * {@link Profile} o {@link NotificationStatus} para permitir acceder de forma
 * consistente a un texto descriptivo asociado a cada valor.
 */
public interface Labeled {

    /**
     * Devuelve el texto legible o etiqueta asociada al valor del enum.
     * <p>
     * Este valor se utiliza normalmente en el frontend o en respuestas API
     * para mostrar una descripción más clara y amigable.
     *
     * @return Etiqueta legible asociada al valor del enum.
     */
    String getLabel();
}
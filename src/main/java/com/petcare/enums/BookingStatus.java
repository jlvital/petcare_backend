package com.petcare.enums;

/**
 * Enum que representa el estado actual de una cita veterinaria.
 * <p>
 * Cada estado refleja el punto en el que se encuentra la cita dentro de su ciclo de vida:
 * <ul>
 *     <li>{@code CONFIRMADA}: Cita activa y pendiente de realización.</li>
 *     <li>{@code COMPLETADA}: Cita realizada con éxito.</li>
 *     <li>{@code CANCELADA}: Cita cancelada por parte del cliente.</li>
 *     <li>{@code ANULADA}: Cita anulada por parte del profesional o por causas internas.</li>
 * </ul>
 */
public enum BookingStatus implements Labeled {

    ANULADA("Anulada"),
    CANCELADA("Cancelada"),
    COMPLETADA("Completada"),
    CONFIRMADA("Confirmada");

    /** Etiqueta legible del estado (para mostrar en el frontend). */
    private final String label;

    BookingStatus(String label) {
        this.label = label;
    }

    /**
     * Devuelve el texto representativo del estado.
     * @return Nombre legible del estado.
     */
    @Override
    public String getLabel() {
        return label;
    }

    /** Valor por defecto para nuevas citas. */
    public static final BookingStatus DEFAULT = CONFIRMADA;

    /**
     * Indica si el estado actual representa una cita finalizada.
     * Se considera finalizada si está {@code COMPLETADA}, {@code CANCELADA} o {@code ANULADA}.
     *
     * @return {@code true} si la cita está finalizada, {@code false} si sigue activa.
     */
    public boolean isFinal() {
        return this == ANULADA || this == CANCELADA || this == COMPLETADA;
    }
}
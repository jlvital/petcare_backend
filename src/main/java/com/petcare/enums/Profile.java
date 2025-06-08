package com.petcare.enums;

/**
 * Enum que representa los distintos perfiles profesionales que puede tener un empleado.
 * <p>
 * Este valor se utiliza para validar si un empleado puede asumir un tipo concreto de cita
 * (por ejemplo, solo los veterinarios pueden aplicar vacunas).
 * <p>
 * Cada perfil está asociado a un conjunto de tipos de cita definidos en {@link BookingType}.
 */
public enum Profile implements Labeled {

    VETERINARIO("Veterinario"),
    AUXILIAR("Auxiliar"),
    TECNICO("Técnico");

    /** Descripción legible del perfil, utilizada para mostrar en la interfaz. */
    private final String label;

    Profile(String label) {
        this.label = label;
    }

    /**
     * Devuelve el nombre legible del perfil.
     * @return Etiqueta del perfil profesional.
     */
    @Override
    public String getLabel() {
        return label;
    }
}
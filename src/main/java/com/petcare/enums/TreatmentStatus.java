package com.petcare.enums;

/**
 * Enum que representa el estado actual de un tratamiento veterinario.
 * <p>
 * Se utiliza para llevar un control del progreso de los tratamientos aplicados a una mascota.
 * El estado puede ser actualizado por los empleados responsables durante el seguimiento clínico.
 * <p>
 * Los posibles valores son:
 * <ul>
 *     <li>{@code INICIADO} – El tratamiento ha sido informado pero aún no se ha aplicado.</li>
 *     <li>{@code EN_CURSO} – El tratamiento está activo y se encuentra en seguimiento.</li>
 *     <li>{@code FINALIZADO} – El tratamiento ha concluido.</li>
 * </ul>
 * Cada estado cuenta con una etiqueta legible para mostrar en la interfaz.
 */
public enum TreatmentStatus implements Labeled {

    INICIADO("Iniciado"),
    EN_CURSO("En curso"),
    FINALIZADO("Finalizado");

    /** Etiqueta visible del estado del tratamiento. */
    private final String label;

    TreatmentStatus(String label) {
        this.label = label;
    }

    /**
     * Devuelve la etiqueta legible asociada al estado del tratamiento.
     *
     * @return Texto como "Iniciado", "En curso" o "Finalizado".
     */
    @Override
    public String getLabel() {
        return label;
    }
}
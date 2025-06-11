package com.petcare.enums;

/**
 * Enum que representa el sexo o género de una mascota.
 * <p>
 * Este valor se usa en el registro y edición de mascotas, y puede ayudar
 * a personalizar recomendaciones, tratamientos o reportes veterinarios.
 * <p>
 * Los valores permitidos son:
 * <ul>
 *     <li>{@code MACHO} – Masculino</li>
 *     <li>{@code HEMBRA} – Femenino</li>
 * </ul>
 * Todos los valores incluyen una etiqueta legible para mostrar en la interfaz.
 */
public enum PetGender implements Labeled {

    MACHO("Macho"),
    HEMBRA("Hembra");

    /** Texto descriptivo que se mostrará al usuario. */
    private final String label;

    PetGender(String label) {
        this.label = label;
    }

    /**
     * Devuelve la etiqueta legible asociada al género.
     * @return Texto visible como "Macho" o "Hembra".
     */
    @Override
    public String getLabel() {
        return label;
    }
}
package com.petcare.enums;

/**
 * Enum que representa los tipos de mascotas más comunes gestionadas en el sistema.
 * <p>
 * Se utiliza en el registro y edición de mascotas para clasificar su especie.
 * Esta información puede ser útil para:
 * <ul>
 *     <li>Filtrar tratamientos o productos específicos</li>
 *     <li>Generar informes veterinarios más precisos</li>
 *     <li>Mostrar iconos o etiquetas personalizadas en la interfaz</li>
 * </ul>
 * <p>
 * Si el tipo de mascota no se encuentra entre las opciones predefinidas,
 * se puede seleccionar {@code OTRO} y especificar un tipo personalizado adicional.
 *
 * <p>
 * Los valores disponibles son:
 * <ul>
 *     <li>{@code PERRO} – Perro</li>
 *     <li>{@code GATO} – Gato</li>
 *     <li>{@code CONEJO} – Conejo</li>
 *     <li>{@code PAJARO} – Pájaro</li>
 *     <li>{@code OTRO} – Tipo no contemplado (permite texto libre)</li>
 * </ul>
 */
public enum PetType implements Labeled {

    PERRO("Perro"),
    GATO("Gato"),
    CONEJO("Conejo"),
    PAJARO("Pájaro"),
    OTRO("Otro");

    /** Texto legible para mostrar en la interfaz. */
    private final String label;

    PetType(String label) {
        this.label = label;
    }

    /**
     * Devuelve la etiqueta legible asociada al tipo de mascota.
     *
     * @return Texto visible como "Perro", "Gato", etc.
     */
    @Override
    public String getLabel() {
        return label;
    }
}
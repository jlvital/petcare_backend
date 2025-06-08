package com.petcare.enums;

/**
 * Enum que representa las distintas categorías de productos disponibles en la tienda del sistema.
 * <p>
 * Esta clasificación permite organizar los productos por tipo, facilitando la navegación,
 * búsqueda y filtrado tanto para clientes como para empleados en el panel de administración.
 * <p>
 * Las categorías actuales incluyen:
 * <ul>
 *     <li>{@code ALIMENTACION} – Productos de comida para mascotas</li>
 *     <li>{@code ACCESORIOS} – Juguetes, correas, camas, comederos, etc.</li>
 *     <li>{@code VACUNAS} – Vacunas que pueden administrarse o comprarse</li>
 * </ul>
 * Todas las opciones están vinculadas a una etiqueta legible que se mostrará en la interfaz.
 */
public enum ProductCategory implements Labeled {

    ALIMENTACION("Alimentación"),
    ACCESORIOS("Accesorios"),
    VACUNAS("Vacunas");

    /** Nombre descriptivo de la categoría para mostrar al usuario. */
    private final String label;

    ProductCategory(String label) {
        this.label = label;
    }

    /**
     * Devuelve la etiqueta legible asociada a la categoría.
     *
     * @return Texto visible como "Alimentación", "Accesorios" o "Vacunas".
     */
    @Override
    public String getLabel() {
        return label;
    }
}
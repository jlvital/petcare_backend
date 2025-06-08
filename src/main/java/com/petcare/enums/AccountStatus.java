package com.petcare.enums;

/**
 * Enum que representa el estado de la cuenta de un usuario en el sistema.
 * <p>
 * Este estado se utiliza para determinar si un cliente o empleado puede acceder a su cuenta
 * y realizar acciones dentro de la plataforma.
 * <p>
 * Los valores posibles son:
 * <ul>
 *     <li>{@code ACTIVA}: la cuenta está en funcionamiento normal y permite acceso total.</li>
 *     <li>{@code BLOQUEADA}: bloqueada temporalmente por errores de acceso o decisión administrativa.</li>
 *     <li>{@code DESACTIVADA}: inactiva por un prolongado tiempo sin uso.</li>
 *     <li>{@code ELIMINADA}: la cuenta es dada de baja, pero no se borran los datos. No permite acceso.</li>
 * </ul>
 */
public enum AccountStatus implements Labeled {

    ACTIVA("Activa"),
    BLOQUEADA("Bloqueada"),
    DESACTIVADA("Desactivada"),
    ELIMINADA("Eliminada");

    /** Etiqueta legible asociada al estado, usada en vistas y mensajes. */
    private final String label;

    AccountStatus(String label) {
        this.label = label;
    }

    /**
     * Devuelve el nombre legible del estado para mostrarlo en el frontend.
     *
     * @return Etiqueta asociada al estado de cuenta.
     */
    @Override
    public String getLabel() {
        return label;
    }

    /**
     * Indica si la cuenta está activa.
     * <p>
     * Este método se utiliza para permitir o restringir operaciones
     * como el acceso al sistema, agendar citas o consultar datos.
     *
     * @return {@code true} si la cuenta está activa; {@code false} en cualquier otro caso.
     */
    public boolean isActive() {
        return this == ACTIVA;
    }
}
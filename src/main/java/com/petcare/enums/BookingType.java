package com.petcare.enums;

/**
 * Enum que representa los distintos tipos de cita que se pueden agendar en el sistema.
 * <p>
 * Cada tipo está asociado a un perfil profesional específico.
 * Esto permite validar si un empleado puede asumir esa cita.
 */
public enum BookingType implements Labeled {

    ANALISIS("Análisis", Profile.VETERINARIO),
    CONSULTA("Consulta general", Profile.VETERINARIO),
    VACUNA("Vacuna", Profile.VETERINARIO),
    BAÑO("Baño", Profile.AUXILIAR),
    PELUQUERIA("Peluquería", Profile.AUXILIAR),
    RADIOGRAFIA("Radiografía", Profile.TECNICO),
    RESONANCIA("Resonancia", Profile.TECNICO);

    /** Texto descriptivo para mostrar en formularios o resultados. */
    private final String label;

    /** Perfil profesional requerido para atender esta cita. */
    private final Profile requiredProfile;

    BookingType(String label, Profile requiredProfile) {
        this.label = label;
        this.requiredProfile = requiredProfile;
    }

    /**
     * Devuelve el nombre visible del tipo de cita.
     * @return Etiqueta legible (ej. "Vacuna").
     */
    @Override
    public String getLabel() {
        return label;
    }

    /**
     * Devuelve el perfil necesario para este tipo de cita.
     * @return Perfil profesional compatible.
     */
    public Profile getRequiredProfile() {
        return requiredProfile;
    }

    /**
     * Verifica si un perfil profesional dado es compatible con este tipo de cita.
     *
     * @param profile Perfil del empleado.
     * @return {@code true} si el perfil es compatible, {@code false} en caso contrario.
     */
    public boolean isCompatibleWith(Profile profile) {
        return requiredProfile.equals(profile);
    }
}
package com.petcare.enums;

	public enum BookingType {
    
	ANALISIS(Profile.VETERINARIO),
    CONSULTA(Profile.VETERINARIO),
    VACUNA(Profile.VETERINARIO),
    BAÑO(Profile.AUXILIAR),
    PELUQUERIA(Profile.AUXILIAR),
    RADIOGRAFIA(Profile.TECNICO),
    RESONANCIA(Profile.TECNICO);

    private final Profile requiredProfile;

    BookingType(Profile requiredProfile) {
        this.requiredProfile = requiredProfile;
    }
    
    public Profile getRequiredProfile() {
        return requiredProfile;
		}
	}
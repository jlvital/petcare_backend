package com.petcare.utils.constants;

/**
 * Constantes relacionadas con los datos del administrador del sistema.
 * Se utilizan durante el arranque inicial o para comprobaciones internas
 */

public final class AdminConstants {

	/**
	 * Constructor privado para evitar la instanciación de esta clase de constantes.
	 */
	
	private AdminConstants() { }
	
	// ╔══════════════════════════════════════╗
	// ║ Datos del administrador 			  ║
	// ╚══════════════════════════════════════╝

	public static final String ADMIN_NAME = "Jose";
	public static final String ADMIN_LASTNAME_1 = "Vital";
	public static final String ADMIN_LASTNAME_2 = "Mascort";
	public static final String ADMIN_FULLNAME = ADMIN_NAME + " " + ADMIN_LASTNAME_1 + " " + ADMIN_LASTNAME_2;
}
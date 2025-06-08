package com.petcare.utils.constants;

import java.time.format.DateTimeFormatter;

/**
 * Constantes generales del sistema PetCare.
 * Incluye configuraciones comunes como codificación, formatos y valores predeterminados globales.
 */

public final class GlobalConstants {

	/**
    * Constructor privado para evitar la instanciación de esta clase de constantes.
    */
	
   private GlobalConstants() { }
	
    // ╔══════════════════════════════════════╗
    // ║ Codificación y formato               ║
    // ╚══════════════════════════════════════╝
    
	public static final String ENCODING_UTF8 = "UTF-8";
    public static final String DATE_PATTERN = "dd/MM/yyyy";
    public static final String TIME_PATTERN = "HH:mm";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);


    // ╔══════════════════════════════════════╗
    // ║ Valores predeterminados              ║
    // ╚══════════════════════════════════════╝
    
    public static final String DEFAULT_ROLE = "CLIENTE";
    public static final String DEFAULT_PROFILE_IMAGE = "default_profile.png";
    public static final String MAIL_DOMAIN = "@petcare.com";
    public static final int BOOKING_DEFAULT_DURATION_MINUTES = 30;
    public static final int ACCOUNT_INACTIVITY_DAYS_LIMIT = 30;
}
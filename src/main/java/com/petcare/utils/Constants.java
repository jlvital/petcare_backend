package com.petcare.utils;

/**
 * Clase de constantes globales del sistema PetCare360º.
 * Agrupa valores fijos utilizados en módulos como seguridad, URLs, usuarios, etc.
 */

public class Constants {

    // ╔════════════════════════════════════════╗
    // ║ 			  DATOS ADMIN				║
    // ╚════════════════════════════════════════╝
    public static final String ADMIN_NAME = "Jose L.";
    public static final String ADMIN_LASTNAME_1 = "Vital";
    public static final String ADMIN_LASTNAME_2 = "Mascort";
    //public static final String SYSTEM_ADMIN_EMAIL = "admin@petcare.com";

    // ╔════════════════════════════════════════╗
    // ║  		CONFIGURACIÓN MAIL ADMIN   		║
    // ╚════════════════════════════════════════╝
    public static final String MAIL_DOMAIN = "@petcare.com";
    public static final String EMAIL_FROM_ADDRESS = "notificaciones.petcare@gmail.com";

    // ╔════════════════════════════════════════╗
    // ║ 		   SEGURIDAD / TOKENS           ║
    // ╚════════════════════════════════════════╝
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_HEADER = "Authorization";

    // ╔════════════════════════════════════════╗
    // ║ 			URLs DEL FRONTEND           ║
    // ╚════════════════════════════════════════╝
    public static final String FRONTEND_BASE_URL = "https://frontend-ten-puce-57.vercel.app/login";
    public static final String LOGIN_URL = FRONTEND_BASE_URL + "/login";
    public static final String PASSWORD_RECOVERY_URL = FRONTEND_BASE_URL + "/recover-password?token=";
    public static final String ACCOUNT_REACTIVATION_URL = FRONTEND_BASE_URL + "/reactivate-account?token=";

    // Métodos para construir enlaces dinámicos
    public static String buildRecoveryLink(String token) {
        return PASSWORD_RECOVERY_URL + token;
    }

    public static String buildReactivationLink(String token) {
        return ACCOUNT_REACTIVATION_URL + token;
    }

    // ╔════════════════════════════════════════╗
    // ║ 	  	  CONFIGURACIÓN GENERAL        	║
    // ╚════════════════════════════════════════╝
    public static final String ENCODING_UTF8 = "UTF-8";
    public static final String DEFAULT_ROLE = "CLIENTE";
    public static final String DEFAULT_PROFILE_IMAGE = "default_profile.png";

    // ╔════════════════════════════════════════╗
    // ║ 		RECUPERACIÓN DE CONTRASEÑA      ║
    // ╚════════════════════════════════════════╝
    public static final int PASSWORD_RESET_EXPIRATION_HOURS = 1;

    // ╔════════════════════════════════════════╗
    // ║ 		TAREAS PROGRAMADAS (CRON)       ║
    // ╚════════════════════════════════════════╝
    public static final int ACCOUNT_INACTIVITY_DAYS_LIMIT = 30;
    public static final String DAILY_ACCOUNT_CHECK_CRON = "0 0 3 * * *"; 
}
package com.petcare.utils.constants;

/**
 * Constantes relacionadas con la gestión de tokens JWT, los parámetros de seguridad y la encriptación.
 */

public final class SecurityConstants {

	 /**
     * Constructor privado para evitar la instanciación de esta clase de constantes.
     */
    private SecurityConstants() { }
    
    // ╔══════════════════════════════════════╗
    // ║ Cabeceras JWT                        ║
    // ╚══════════════════════════════════════╝
    
	public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    // ╔══════════════════════════════════════╗
    // ║ Rutas de claves JWT                  ║
    // ╚══════════════════════════════════════╝
    
    public static final String JWT_KEY_PATH = "./config/jwt.key";
    public static final String JWT_HASH_PATH = "./config/jwt.key.sha256";

    // ╔══════════════════════════════════════╗
    // ║ Seguridad y encriptación             ║
    // ╚══════════════════════════════════════╝

    public static final int BCRYPT_STRENGTH = 8;
    public static final int MAX_LOGIN_ATTEMPTS = 3;

    // ╔══════════════════════════════════════╗
    // ║ Expiración y lógica de cuenta        ║
    // ╚══════════════════════════════════════╝
    
    public static final int PASSWORD_RESET_EXPIRATION_HOURS = 1;
    public static final int PASSWORD_RESET_TOKEN_EXPIRATION_MINUTES = 30;
    public static final int ACCOUNT_REACTIVATION_TOKEN_HOURS = 24;
    public static final int ACCOUNT_BLOCKED_TOKEN_HOURS = 1;
    public static final String DEFAULT_PASSWORD = "¡1q2W3e4R?";
}
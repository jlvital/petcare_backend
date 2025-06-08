package com.petcare.utils.constants;

/**
 * Rutas públicas, recursos estáticos y URLs del frontend necesarias para la
 * redirección y la navegación.
 * También incluye métodos utilitarios para construir enlaces personalizados.
 */

public final class UrlConstants {

	/**
     * Constructor privado para evitar la instanciación de esta clase utilitaria.
     */
    private UrlConstants() { } 
    
	// ╔══════════════════════════════════════╗
	// ║ Rutas públicas API					  ║
	// ╚══════════════════════════════════════╝

	public static final String[] PUBLIC_PATHS = { "/", "/auth/**", "/login", "/register", "/forgot-password",
			"/reset-password", "/productos", "/login-success" };

	// ╔══════════════════════════════════════╗
	// ║ Recursos estáticos					  ║
	// ╚══════════════════════════════════════╝

	public static final String[] STATIC_RESOURCES = { "/assets/**", "/favicon.ico" };

	// ╔══════════════════════════════════════╗
	// ║ URLs del frontend					  ║
	// ╚══════════════════════════════════════╝

	public static final String FRONTEND_BASE_URL = "http://localhost:5173";

	public static final String LOGIN_URL = FRONTEND_BASE_URL + "/login";
	public static final String RECOVERY_URL = FRONTEND_BASE_URL + "/recover-account?token=";
	public static final String FRONT_LOGIN_SUCCESS_URL = FRONTEND_BASE_URL + "/login-success?token=";
	public static final String FRONT_LOGIN_ERROR_URL = FRONTEND_BASE_URL + "/login-error";

	// ╔══════════════════════════════════════╗
	// ║ Rutas dinámicas					  ║
	// ╚══════════════════════════════════════╝

	public static String buildRecoveryLink(String token) {
		return RECOVERY_URL + token;
	}

	public static String buildReactivationLink(String token) {
		return RECOVERY_URL + token;
	}
}
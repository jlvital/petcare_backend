package com.petcare.utils;

import com.petcare.domain.user.User;
import com.petcare.enums.Role;

/**
 * Utilidad para verificar el rol asignado a un usuario.
 * <p>
 * Permite validar si un usuario tiene un rol específico o uno de los roles principales del sistema:
 * {@code ADMIN}, {@code CLIENTE} o {@code EMPLEADO}.
 * </p>
 */

public final class RoleUtils {

	 /**
     * Constructor privado para evitar la instanciación de la clase.
     */
	
	private RoleUtils() { }

	/**
     * Comprueba si el usuario tiene el rol especificado.
     *
     * @param user         Usuario a comprobar (puede ser {@code null}).
     * @param expectedRole Rol que se espera tenga el usuario.
     * @return {@code true} si el usuario tiene el rol esperado, {@code false} en caso contrario o si hay {@code null}.
     */
	
	public static boolean is(User user, Role expectedRole) {
		if (user == null || expectedRole == null) {
			return false;
		}

		Role actualRole = user.getRole();
		if (actualRole == null) {
			return false;
		}

		return expectedRole.equals(actualRole);
	}

	/**
     * Comprueba si el usuario es administrador.
     *
     * @param user Usuario a comprobar.
     * @return {@code true} si tiene rol {@code ADMIN}.
     */
	
	public static boolean isAdmin(User user) {
		return is(user, Role.ADMIN);
	}

	/**
     * Comprueba si el usuario es empleado.
     *
     * @param user Usuario a comprobar.
     * @return {@code true} si tiene rol {@code EMPLEADO}.
     */
	
	public static boolean isEmployee(User user) {
		return is(user, Role.EMPLEADO);
	}

	/**
     * Comprueba si el usuario es cliente.
     *
     * @param user Usuario a comprobar.
     * @return {@code true} si tiene rol {@code CLIENTE}.
     */
	
	public static boolean isClient(User user) {
		return is(user, Role.CLIENTE);
	}
}
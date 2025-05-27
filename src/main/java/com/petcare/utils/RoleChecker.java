package com.petcare.utils;

import com.petcare.enums.Role;
import com.petcare.model.user.User;

/**
 * Utilidad centralizada para comprobar el rol de un usuario.
 */
public final class RoleChecker {

	private RoleChecker() {
		// Clase utilitaria: no instanciable
	}

	/** Comprobación genérica de rol */
	public static boolean is(User user, Role expectedRole) {
		return user != null && expectedRole != null && expectedRole.equals(user.getRole());
	}

	/** Comprobaciones específicas */
	public static boolean isAdmin(User user) {
		return is(user, Role.ADMIN);
	}

	public static boolean isEmployee(User user) {
		return is(user, Role.EMPLEADO);
	}

	public static boolean isClient(User user) {
		return is(user, Role.CLIENTE);
	}
}
package com.petcare.validations;

import com.petcare.model.user.User;
import com.petcare.utils.RoleChecker;
public class UserValidator {

	public static void validateUsernameAndRecoveryEmail(User user) {
	    if (user == null) {
	        throw new IllegalArgumentException("No se puede validar un usuario nulo.");
	    }

	    String username = user.getUsername();
	    String recoveryEmail = user.getRecoveryEmail();

	    if (RoleChecker.isClient(user)) {
	        if (!username.equals(recoveryEmail)) {
	            throw new IllegalArgumentException("Para clientes, username y correo de recuperación deben ser iguales.");
	        }
	    }

	    if (RoleChecker.isEmployee(user)) {
	        if (username.equals(recoveryEmail)) {
	            throw new IllegalArgumentException("Para empleados, username (corporativo) y correo personal deben ser distintos.");
	        }
	    }
	}
}

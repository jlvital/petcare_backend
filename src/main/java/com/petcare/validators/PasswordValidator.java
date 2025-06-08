package com.petcare.validators;

import org.springframework.stereotype.Component;
import com.petcare.exceptions.PasswordException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PasswordValidator {

	/**
	 * Valida que las contraseñas coincidan.
	 *
	 * @param password    La contraseña esperada
	 * @param newPassword La nueva contraseña que se quiere confirmar
	 * @throws PasswordException si las contraseñas no coinciden
	 */
	
	public void validatePasswordsMatch(String password, String newPassword) {
		if (password == null || newPassword == null || !password.equals(newPassword)) {
			log.warn("Las contraseñas no coinciden: [Password={} ≠ New Password={}]", password, newPassword);
			throw new PasswordException(
					"Las contraseñas no coinciden. Asegúrate de escribir la misma contraseña dos veces.");
		}
	}
}
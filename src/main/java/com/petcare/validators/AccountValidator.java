package com.petcare.validators;

import com.petcare.exceptions.AccountException;
import com.petcare.domain.user.User;

/**
 * Clase utilitaria para validar el estado de una cuenta de usuario.
 */
public class AccountValidator {

    /**
     * Comprueba que la cuenta esté activa antes de permitir acciones protegidas.
     *
     * @param user el usuario a validar
     * @throws AccountException si la cuenta no está activa
     */
    public static void validateAccountIsActive(User user) {
        if (user == null) {
            throw new AccountException("Se requiere un usuario válido para verificar el estado de la cuenta.");
        }

        if (!user.getAccountStatus().isActive()) {
            throw new AccountException("Cuenta temporalmente desactivada.");
        }
    }
}
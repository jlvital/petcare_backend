package com.petcare.utils;

import com.petcare.enums.Role;
import com.petcare.model.user.User;

/**
 * Clase utilitaria que centraliza la verificación de roles de usuarios.
 * Evita el uso disperso de instanceof o comparaciones directas con enums.
 */

public class RoleChecker {

    private RoleChecker() {
        /* Constructor privado para que no se instancie */
    }

    public static boolean isClient(User user) {
        return user != null && user.getRole() == Role.CLIENTE;
    }

    public static boolean isEmployee(User user) {
        return user != null && user.getRole() == Role.EMPLEADO;
    }

    public static boolean isAdmin(User user) {
        return user != null && user.getRole() == Role.ADMIN;
    }
}
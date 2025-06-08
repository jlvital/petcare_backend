package com.petcare.utils;

import com.petcare.domain.user.User;

/**
 * Clase utilitaria para formatear nombres de usuarios {@link User}.
 * <p>
 * Proporciona métodos estáticos para obtener:
 * <ul>
 *     <li>El nombre simple del usuario</li>
 *     <li>El nombre junto al primer apellido</li>
 *     <li>El nombre completo (nombre + primer y segundo apellido)</li>
 * </ul>
 * Esta clase no es instanciable.
 */

public final class NameFormatter {
	
	 /**
     * Constructor privado para evitar la instanciación de la clase.
     */
	
	private NameFormatter() { }

	 /**
     * Devuelve el nombre simple del usuario.
     *
     * @param user Objeto {@link User} del cual extraer el nombre.
     * @return El nombre del usuario sin espacios extra, o {@code null} si el usuario o su nombre son {@code null}.
     */
	
    public static String getFirstName(User user) {
        if (user == null) {
            return null;
        }

        String name = user.getName();
        if (name != null) {
            return name.trim();
        } else {
            return null;
        }
    }

    /**
     * Devuelve el nombre seguido del primer apellido del usuario.
     * <p>Si no existe primer apellido, devuelve solo el nombre.</p>
     *
     * @param user Objeto {@link User} del cual extraer los datos.
     * @return Cadena con "nombre + primer apellido" (si existe), o solo el nombre si no hay apellido,
     *         o {@code null} si el usuario o su nombre son {@code null}.
     */
    
    public static String getShortFullName(User user) {
        if (user == null || user.getName() == null) {
            return null;
        }

        String name = user.getName().trim();
        String lastName1 = user.getLastName1();

        if (lastName1 != null && !lastName1.isBlank()) {
            return name + " " + lastName1.trim();
        } else {
            return name;
        }
    }

    /**
     * Devuelve el nombre completo del usuario: nombre, primer apellido y segundo apellido.
     * <p>Los apellidos se incluyen solo si existen y no están vacíos.</p>
     *
     * @param user Objeto {@link User} del cual extraer los datos.
     * @return Cadena con el nombre completo formateado, o {@code null} si el usuario o su nombre son {@code null}.
     */
    
    public static String getFullName(User user) {
        if (user == null || user.getName() == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder(user.getName().trim());

        if (user.getLastName1() != null && !user.getLastName1().isBlank()) {
            sb.append(" ").append(user.getLastName1().trim());
        }

        if (user.getLastName2() != null && !user.getLastName2().isBlank()) {
            sb.append(" ").append(user.getLastName2().trim());
        }

        return sb.toString();
    }
}
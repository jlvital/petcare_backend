package com.petcare.utils;

import java.security.SecureRandom;
import java.util.regex.Pattern;

import com.petcare.exceptions.BusinessException;

/**
 * Utilidad encargada de generar contraseñas aleatorias y seguras.
 * <p>
 * Diseñada para crear contraseñas temporales que pueden ser utilizadas
 * durante procesos como recuperación de cuenta, activación inicial, etc.
 * </p>
 * <p>
 * Esta clase es no instanciable y expone únicamente métodos estáticos.
 * </p>
 */
public final class PasswordUtil {

    /**
     * Constructor privado para evitar la instanciación de la clase.
     */
    private PasswordUtil() {}

    /**
     * Cadena de caracteres permitidos para construir contraseñas.
     */
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*";

    /**
     * Expresión regular que garantiza al menos:
     * - Una letra mayúscula
     * - Un carácter especial entre !@#$%^&*?-+
     */
    private static final Pattern PASSWORD_PATTERN = 
        Pattern.compile("^(?=.*[A-Z])(?=.*[!@#$%^&*?\\-+]).+$");

    /**
     * Genera una contraseña aleatoria que cumpla las condiciones mínimas de seguridad.
     * <p>
     * Internamente se genera una contraseña aleatoria y se verifica que cumpla
     * con la expresión regular definida. Si no la cumple, se vuelve a intentar.
     * </p>
     *
     * @param length Longitud deseada. Debe ser al menos 8.
     * @return Contraseña segura generada aleatoriamente.
     * @throws BusinessException si se indica una longitud inferior a 8.
     */
    public static String generateRandomPassword(int length) {
        if (length < 8) {
            throw new BusinessException("La contraseña debe tener al menos 8 caracteres.");
        }

        SecureRandom random = new SecureRandom();
        String password;

        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                int index = random.nextInt(CHARACTERS.length());
                sb.append(CHARACTERS.charAt(index));
            }
            password = sb.toString();
        } while (!PASSWORD_PATTERN.matcher(password).matches());

        return password;
    }
}
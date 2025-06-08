package com.petcare.validators;

import com.petcare.domain.employee.Employee;
import com.petcare.domain.user.User;
import com.petcare.domain.user.UserRepository;
import com.petcare.exceptions.*;

import java.text.Normalizer;
import java.util.Objects;
import java.util.Optional;

import com.petcare.utils.RoleUtils;
import com.petcare.utils.constants.GlobalConstants;

public class EmployeeValidator {

	/**
	 * Valida que el usuario autenticado sea un empleado y que el ID proporcionado coincida con su ID.
	 * <p>
	 * En caso de que el usuario no tenga rol EMPLEADO, su ID sea nulo o no coincida con el ID recibido,
	 * se lanza una excepción de autorización.
	 *
	 * @param user       Usuario autenticado.
	 * @param employeeId ID que se intenta consultar o modificar.
	 * @throws UnauthorizedException si el usuario no tiene rol EMPLEADO, su ID es nulo,
	 *                                o el ID no coincide con el proporcionado.
	 */
	public static void validateEmployeeAccess(User user, Long employeeId) {
	    if (!RoleUtils.isEmployee(user) || user.getId() == null || !user.getId().equals(employeeId)) {
	        throw new UnauthorizedException("Acceso denegado: no tienes permisos para consultar los datos de la cita.");
	    }
	}

	/**
	 * Verifica que el empleado autenticado no sea nulo ni tenga ID nulo.
	 * <p>
	 * Si la validación falla, lanza una {@link UnauthorizedException}.
	 *
	 * @param employee Empleado autenticado a validar.
	 * @throws UnauthorizedException si el empleado es nulo o no tiene ID.
	 */
	public static void validateAuthenticatedEmployee(Employee employee) {
	    if (employee == null || employee.getId() == null) {
	        throw new UnauthorizedException("Acceso denegado: no se ha podido autenticar al empleado.");
	    }
	}
	
    /**
     * Obtiene un empleado a partir de su ID.
     * Lanza una excepción si no se encuentra o no corresponde a un empleado.
     */
    public static Employee requireEmployeeById(UserRepository repository, Long id) {
        Optional<User> optional = repository.findById(id);
        if (optional.isEmpty() || !(optional.get() instanceof Employee)) {
            throw new DataException("No se ha encontrado ningún empleado con el ID proporcionado. Verifica la información e inténtalo de nuevo.");
        }
        return (Employee) optional.get();
    }

    /**
     * Obtiene un empleado a partir de su username (correo corporativo).
     * Lanza una excepción si no se encuentra o no corresponde a un empleado.
     */
    public static Employee requireEmployeeByUsername(UserRepository repository, String username) {
        Optional<User> optional = repository.findByUsername(username);
        if (optional.isEmpty() || !(optional.get() instanceof Employee)) {
            throw new DataException("No se ha encontrado ningún empleado con ese nombre de usuario. Revisa los datos e inténtalo de nuevo.");
        }
        return (Employee) optional.get();
    }

    /**
     * Verifica que el correo de recuperación no esté ya registrado por otro usuario.
     */
    public static void checkRecoveryEmail(UserRepository userRepository, User currentUser, String recoveryEmail) {
        if (recoveryEmail == null) return;

        userRepository.findByRecoveryEmail(recoveryEmail).ifPresent(existingUser -> {
            if (!Objects.equals(existingUser.getId(), currentUser.getId())) {
                throw new BusinessException("El correo electrónico indicado ya está asociado a otro usuario del istema. Por favor, utiliza uno diferente.");
            }
        });
    }

    /**
     * Genera un nombre de usuario único para empleados sin acentos, espacios ni caracteres especiales.
     * Se asegura de que el correo generado no exista ya en la base de datos.
     *
     * @param userRepository Repositorio para verificar duplicados.
     * @param name           Nombre del empleado.
     * @param lastName1      Primer apellido.
     * @param lastName2      Segundo apellido.
     * @return Correo corporativo único en formato normalizado.
     */
    public static String generateUniqueCorporateEmail(UserRepository userRepository, String name, String lastName1, String lastName2) {
        String baseUsername = generateNormalizedUsername(name, lastName1, lastName2);
        String fullEmail = baseUsername + GlobalConstants.MAIL_DOMAIN;

        int i = 1;
        while (userRepository.existsByUsername(fullEmail)) {
            fullEmail = baseUsername + i + GlobalConstants.MAIL_DOMAIN;
            i++;
        }

        return fullEmail;
    }

    /**
     * Genera el nombre base para el correo corporativo: nombre.apellido1.apellido2
     * con caracteres normalizados (sin tildes ni símbolos).
     */
    private static String generateNormalizedUsername(String name, String lastName1, String lastName2) {
        String n = normalizeText(name);
        String l1 = normalizeText(lastName1);
        String l2 = normalizeText(lastName2);
        return n + "." + l1 + "." + l2;
    }

    /**
     * Elimina tildes y caracteres especiales de un texto, dejando solo letras en minúscula.
     */
    private static String normalizeText(String text) {
        if (text == null) return "";
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized
                .replaceAll("\\p{M}", "")         // Elimina acentos
                .replaceAll("[^a-zA-Z]", "")      // Elimina todo lo que no sea letra
                .toLowerCase();                   // Convierte a minúsculas
    }
}
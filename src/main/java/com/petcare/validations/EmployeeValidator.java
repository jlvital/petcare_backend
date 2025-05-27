package com.petcare.validations;

import java.util.Optional;

import com.petcare.enums.BookingType;
import com.petcare.enums.Profile;
import com.petcare.exceptions.UserAlreadyExistsException;
import com.petcare.model.employee.Employee;
import com.petcare.model.user.User;
import com.petcare.model.user.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmployeeValidator {

	/**
     * Valida que el correo de recuperación que se quiere establecer no esté en uso por otro usuario.
     * Solo se ejecuta si el nuevo correo no es nulo y es diferente del actual.
     */
    public static void validateRecoveryEmailUniqueness(UserRepository repository, Employee employee, String newEmail) {
        if (newEmail == null || newEmail.trim().isEmpty()) {
            log.info("Correo de recuperación nulo o vacío. No se realiza validación.");
            return;
        }

        // Si el correo no cambia, no se valida
        String current = employee.getRecoveryEmail();
        if (current != null && current.equalsIgnoreCase(newEmail)) {
            log.info("Correo de recuperación no ha cambiado para el empleado ID {}", employee.getId());
            return;
        }

        Optional<User> existing = repository.findByRecoveryEmail(newEmail);
        if (existing.isPresent()) {
            User found = existing.get();
            if (!found.getId().equals(employee.getId())) {
                log.warn("El correo de recuperación '{}' ya está en uso por otro usuario (ID {}).", newEmail, found.getId());
                throw new UserAlreadyExistsException("Ya existe un usuario con ese correo de contacto.");
            }
        }

        log.info("Correo de recuperación '{}' válido para empleado ID {}", newEmail, employee.getId());
    }
    /**
     * Verifica si un perfil de empleado es válido para un tipo de cita.
     *
     * @param bookingType Tipo de cita
     * @param employeeProfile Perfil del empleado
     * @throws IllegalArgumentException si el perfil no es válido
     */
    public static void validateProfile(BookingType bookingType, Profile employeeProfile) {
        if (bookingType == null || employeeProfile == null) {
            log.warn("Validación de perfil fallida: tipo de cita o perfil del empleado no proporcionado.");
            throw new IllegalArgumentException("Tipo de cita o perfil del empleado no puede ser nulo.");
        }

        Profile requerido = bookingType.getRequiredProfile();

        if (!requerido.equals(employeeProfile)) {
            log.warn("Perfil no válido: se esperaba un perfil [{}] para citas de tipo [{}], pero se recibió [{}].",
                    requerido.name(), bookingType.name(), employeeProfile.name());
            throw new IllegalArgumentException("El profesional no tiene el perfil adecuado para el tipo de cita seleccionado.");
        }

        log.info("Validación correcta: el perfil [{}] es válido para atender citas de tipo [{}].",
                employeeProfile.name(), bookingType.name());
    }

    /**
     * Verifica si el empleado tiene un perfil válido para el tipo de cita especificado.
     *
     * @param bookingType Tipo de cita
     * @param employee Empleado a validar
     * @throws IllegalArgumentException si el perfil del empleado no es válido
     */
    public static void validateEmployee(BookingType bookingType, Employee employee) {
        if (employee == null) {
            log.warn("Validación fallida: el empleado proporcionado es nulo.");
            throw new IllegalArgumentException("Debes seleccionar un profesional válido.");
        }

        if (employee.getProfile() == null) {
            log.warn("Validación fallida: el empleado [{}] no tiene un perfil asignado.", employee.getId());
            throw new IllegalArgumentException("El profesional seleccionado no tiene un perfil asignado.");
        }

        log.debug("Validando si el empleado [{}] con perfil [{}] puede atender una cita de tipo [{}]...",
                employee.getId(), employee.getProfile().name(), bookingType != null ? bookingType.name() : "NULO");

        validateProfile(bookingType, employee.getProfile());
    }
}
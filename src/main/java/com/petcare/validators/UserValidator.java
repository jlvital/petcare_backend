package com.petcare.validators;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import com.petcare.domain.user.User;
import com.petcare.domain.user.UserRepository;
import com.petcare.exceptions.*;
import com.petcare.utils.RoleUtils;

/**
 * Validador central para verificar datos relacionados con usuarios.
 */
public class UserValidator {

    /**
     * Verifica que el correo de acceso (username) no esté vacío ni repetido.
     *
     * @param username correo del usuario
     * @param userRepository repositorio de usuarios
     * @throws DataException si el username es nulo o vacío
     * @throws AlreadyExistsException si ya existe otro usuario con ese username
     */
    public static void checkUsername(String username, UserRepository userRepository) {
        if (username == null || username.trim().isEmpty()) {
            throw new DataException("El correo de acceso es obligatorio. Por favor, introduce uno válido.");
        }

        if (userRepository.existsByUsername(username)) {
            throw new AlreadyExistsException("Ya existe un usuario registrado con ese correo. Intenta iniciar sesión o usa otro diferente.");
        }
    }

    /**
     * Reglas de consistencia entre username y correo de recuperación según el tipo de usuario.
     *
     * @param user usuario a validar
     * @throws DataException si el usuario es nulo o no cumple las reglas de coherencia
     */
    public static void validateUsernameAndRecoveryEmail(User user) {
        if (user == null) {
            throw new DataException("Ha ocurrido un error inesperado al validar tu cuenta. Por favor, inténtalo de nuevo más tarde.");
        }

        String username = user.getUsername();
        String recoveryEmail = user.getRecoveryEmail();

        if (RoleUtils.isClient(user)) {
            if (!username.equals(recoveryEmail)) {
                throw new DataException("Tu correo de acceso y el de recuperación deben coincidir.");
            }
        }

        if (RoleUtils.isEmployee(user)) {
            if (username.equals(recoveryEmail)) {
                throw new DataException("El correo corporativo y el personal deben ser diferentes.");
            }
        }
    }

    /**
     * Busca un usuario por ID o lanza una excepción si no existe.
     *
     * @param repository repositorio de usuarios
     * @param userId ID del usuario
     * @return el usuario encontrado
     * @throws NotFoundException si no existe ningún usuario con ese ID
     */
    public static User requireUserById(UserRepository repository, Long userId) {
        Optional<User> optionalUser = repository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("No se ha encontrado ningún usuario con el ID proporcionado.");
        }
        return optionalUser.get();
    }
    
    public static User requireUserByUsername(UserRepository repository, String username) {
        Optional<User> optionalUser = repository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("No se ha encontrado ningún usuario con el correo proporcionado.");
        }
        return optionalUser.get();
    }
    
    /**
     * Actualiza el campo lastAccess si ha pasado al menos un minuto desde el último acceso registrado.
     *
     * @param user el usuario autenticado
     * @return true si se ha actualizado el campo, false si no fue necesario
     */
    public static boolean updateLastAccess(User user) {
        if (user == null) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime last = user.getLastAccess();

        if (last == null || Duration.between(last, now).toSeconds() >= 60) {
            user.setLastAccess(now);
            return true;
        }

        return false;
    }
}

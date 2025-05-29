package com.petcare.validations;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.petcare.exceptions.UserAlreadyExistsException;
import com.petcare.model.client.Client;
import com.petcare.model.client.dto.ClientRegisterRequest;
import com.petcare.model.user.User;
import com.petcare.model.user.UserRepository;

public class ClientValidator {

    private static final Logger log = LoggerFactory.getLogger(ClientValidator.class);

    /**
     * Verifica que el cliente esté autenticado correctamente.
     * Lanza excepción si es nulo o su ID no es válido.
     */
    public static void validateAuthenticatedClient(Client client) {
        if (client == null) {
            log.warn("Acceso no permitido: no se encontró un cliente autenticado (cliente es nulo).");
            throw new IllegalArgumentException("No estás autenticado como cliente.");
        }

        if (client.getId() == null || client.getId() <= 0) {
            log.warn("Acceso denegado: cliente autenticado sin ID válido. ID actual: {}", client.getId());
            throw new IllegalArgumentException("No se ha podido identificar correctamente al cliente.");
        }

        log.info("Cliente autenticado: ID [{}]", client.getId());
    }

    /**
     * Verifica que el cliente tenga un número de teléfono registrado y válido.
     */
    public static void validatePhoneNumber(Client client) {
        if (client == null) {
            log.warn("Error interno: intento de validación de teléfono con cliente nulo.");
            throw new IllegalArgumentException("No se puede validar el teléfono porque no se ha recibido un cliente.");
        }
/*
        String phone = client.getPhoneNumber();
        if (phone == null || phone.trim().isEmpty()) {
            log.warn("Cliente ID [{}] no tiene teléfono registrado. Requiere número válido para WhatsApp.", client.getId());
            throw new IllegalArgumentException("Debes registrar un número de teléfono para recibir notificaciones por WhatsApp.");
        }

        log.info("Cliente ID [{}] tiene teléfono registrado: {}", client.getId(), phone);*/
    }
        
    public static void validateRegisterRequest(ClientRegisterRequest request, UserRepository repository) {
        if (request == null) {
            log.warn("Intento de validación con datos nulos.");
            throw new IllegalArgumentException("Los datos del cliente no pueden ser nulos.");
        }

        String username = request.getUsername();
        String recoveryEmail = request.getRecoveryEmail();

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo de acceso es obligatorio.");
        }

        if (recoveryEmail == null || recoveryEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo de contacto es obligatorio.");
        }

        if (repository.existsByUsername(username)) {
            log.warn("Intento de registro duplicado con username: {}", username);
            throw new UserAlreadyExistsException("Ya existe un usuario con ese correo de acceso.");
        }

        // Validación sin programación funcional
		Optional<User> existing = repository.findByRecoveryEmail(recoveryEmail);
        if (existing.isPresent()) {
            log.warn("Correo de recuperación ya registrado: {}", recoveryEmail);
            throw new UserAlreadyExistsException("Ya existe un usuario con ese correo de contacto.");
        }

        if (username.equalsIgnoreCase(recoveryEmail)) {
            log.info("El cliente usará el mismo correo para acceso y recuperación: {}", username);
        }
    }
}
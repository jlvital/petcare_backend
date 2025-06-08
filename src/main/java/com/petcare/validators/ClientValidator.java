package com.petcare.validators;

import java.util.Optional;

import com.petcare.domain.client.Client;
import com.petcare.domain.client.dto.ClientRequest;
import com.petcare.domain.client.dto.ClientUpdate;
import com.petcare.domain.user.UserRepository;
import com.petcare.exceptions.AlreadyExistsException;
import com.petcare.exceptions.AuthenticationException;
import com.petcare.exceptions.BusinessException;
import com.petcare.exceptions.UnauthorizedException;
import com.petcare.exceptions.DataException;
import com.petcare.exceptions.NotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientValidator {

	/**
	 * Valida que el usuario autenticado sea un cliente y realiza el cast.
	 *
	 * @param user Usuario autenticado
	 * @return Cliente autenticado
	 * @throws UnauthorizedException si el usuario no es un cliente
	 */
	public static Client validateUserIsClient(com.petcare.domain.user.User user) {
	    if (user == null || !(user instanceof Client)) {
	        log.warn("Acceso denegado: se requiere un cliente autenticado, pero se recibió: {}", user != null ? user.getClass().getSimpleName() : "null");
	        throw new UnauthorizedException("Acceso denegado. Esta funcionalidad solo está disponible para clientes.");
	    }

	    Client client = (Client) user;
	    if (client.getId() == null || client.getId() <= 0) {
	        log.warn("Cliente autenticado con ID no válido. ID actual: {}", client.getId());
	        throw new AuthenticationException("No se ha podido verificar tu identidad como cliente.");
	    }

	    log.info("Cliente autenticado correctamente (ID: {})", client.getId());
	    return client;
	}
 
	
	/**
     * Obtiene un cliente a partir de su ID.
     * Lanza una excepción si no se encuentra o no corresponde a un empleado.
     */
	
	public static Client requireClientById(UserRepository repository, Long id) {
	    Optional<com.petcare.domain.user.User> optional = repository.findById(id);
	    if (optional.isEmpty() || !(optional.get() instanceof Client)) {
	        throw new NotFoundException("No se ha encontrado ningún empleado con el ID proporcionado. Verifica la información e inténtalo de nuevo.");
	    }
	    return (Client) optional.get();
	}
	
	/**
     * Verifica que el cliente esté autenticado correctamente.
     * Lanza excepción si es nulo o su ID no es válido.
     */
    public static void validateAuthenticatedClient(Client client) {
        if (client == null) {
            log.warn("Acceso no permitido: no se encontró un cliente autenticado.");
            throw new AuthenticationException("Debes estar autenticado para realizar esta acción. Por favor, inicia sesión e inténtalo de nuevo.");
        }

        if (client.getId() == null || client.getId() <= 0) {
            log.warn("Cliente autenticado sin ID válido. ID actual: {}", client.getId());
            throw new AuthenticationException("No se ha podido verificar tu identidad. Intenta iniciar sesión nuevamente.");
        }

        log.info("Cliente autenticado correctamente: ID [{}]", client.getId());
    }

    /**
     * Verifica que los datos de registro del cliente sean válidos y no duplicados.
     */
    public static void validateRegisterRequest(ClientRequest request, UserRepository repository) {
        if (request == null) {
            log.warn("Intento de registro con datos nulos.");
            throw new DataException("Debes completar el formulario de registro antes de continuar.");
        }

        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            log.warn("Correo de acceso vacío o nulo en la solicitud de registro.");
            throw new DataException("Debes indicar un correo válido para completar el registro.");
        }

        if (repository.existsByUsername(request.getUsername())) {
            log.warn("Ya existe un usuario registrado con el correo: {}", request.getUsername());
            throw new AlreadyExistsException("El usuario" + request.getUsername() + " ya existe. Intenta iniciar sesión o usa otro correo.");
        }
    }
    
    /**
     * Valida los datos del formulario de actualización del cliente.
     */
    public static void validateUpdateRequest(ClientUpdate request) {
    	if (request == null) {
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La solicitud está vacía.");
    	}

    	String phone = request.getPhoneNumber();
    	if (phone != null && !phone.isBlank()) {
    	    if (!phone.matches("^\\d{9}$")) {
    	        throw new BusinessException("El teléfono debe tener 9 dígitos.");
    	    }
    	}
    }
}
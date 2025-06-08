package com.petcare.validators;

import java.util.Optional;

import com.petcare.domain.booking.Booking;
import com.petcare.domain.client.Client;
import com.petcare.domain.pet.Pet;
import com.petcare.domain.pet.PetRepository;
import com.petcare.domain.pet.dto.PetRequest;
import com.petcare.domain.pet.dto.PetUpdate;
import com.petcare.enums.PetGender;
import com.petcare.enums.PetType;
import com.petcare.exceptions.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PetValidator {

	// ╔════════════════════════════════════════════════════════════╗
	// ║ VALIDACIONES DE PROPIEDAD DE MASCOTAS ║
	// ╚════════════════════════════════════════════════════════════╝

	/**
	 * Verifica si una mascota pertenece al cliente autenticado. Muy útil cuando ya
	 * se ha recuperado la entidad {@code Pet}.
	 *
	 * @param client Cliente autenticado
	 * @param pet    Mascota a validar
	 * @throws UnauthorizedException si la mascota no pertenece al cliente
	 */
	public static void checkOwner(Client client, Pet pet) {
		if (client == null || pet == null || pet.getClient() == null) {
			log.warn("No se puede validar la propiedad: cliente o mascota no asignados correctamente.");
			throw new UnauthorizedException("No se ha podido comprobar si la mascota está registrada a tu nombre.");
		}

		Long clientId = client.getId();
		Long ownerId = pet.getClient().getId();

		if (!clientId.equals(ownerId)) {
			log.warn("Cliente ID [{}] intentó acceder a mascota ID [{}] que pertenece a ID [{}].", clientId,
					pet.getId(), ownerId);
			throw new UnauthorizedException("Esta mascota no está registrada a tu nombre, " + client.getName() + ".");
		}

		log.info("Mascota ID [{}] validada correctamente. Propietario: cliente ID [{}].", pet.getId(), clientId);
	}

	/**
	 * Verifica que la mascota asociada a una cita pertenezca al cliente. Variante
	 * del método anterior para cuando se trabaja con {@code Booking}.
	 *
	 * @param client  Cliente autenticado
	 * @param booking Cita que contiene la mascota
	 * @throws UnauthorizedException si la mascota no pertenece al cliente
	 */
	public static void checkOwner(Client client, Booking booking) {
		if (client == null || booking == null || booking.getPet() == null || booking.getPet().getClient() == null) {
			log.warn("No se puede validar la propiedad de la mascota desde cita.");
			throw new UnauthorizedException(
					"No hemos podido verificar si esta cita corresponde a una de tus mascotas.");
		}

		checkOwner(client, booking.getPet());
	}

	/**
	 * Comprueba si una mascota pertenece al cliente, usando sus identificadores.
	 * Ideal cuando ya se tiene la entidad {@code Pet} y solo se conoce el ID del
	 * cliente.
	 *
	 * @param pet      Mascota a validar
	 * @param clientId ID del cliente autenticado
	 * @throws UnauthorizedException si la mascota no pertenece al cliente
	 */
	public static void checkOwner(Pet pet, Long clientId) {
		if (pet == null || pet.getClient() == null || !pet.getClient().getId().equals(clientId)) {
			log.warn("La mascota ID [{}] no pertenece al cliente ID [{}]", pet != null ? pet.getId() : "null",
					clientId);
			throw new UnauthorizedException("Esta mascota no está asociada a tu perfil. Selecciona una válida.");
		}

		log.info("Mascota ID [{}] pertenece al cliente ID [{}]. Validación de propiedad correcta.", pet.getId(),
				clientId);
	}

	// ╔════════════════════════════════════════════════════════════╗
	// ║ VALIDACIONES DE EXISTENCIA EN BBDD ║
	// ╚════════════════════════════════════════════════════════════╝

	/**
	 * Verifica que la mascota con el ID indicado exista en base de datos.
	 *
	 * @param repo  Repositorio de mascotas
	 * @param petId ID de la mascota
	 * @return Mascota encontrada
	 * @throws NotFoundException si no existe
	 */
	public static Pet validatePetExists(PetRepository repo, Long petId) {
		Optional<Pet> petOpt = repo.findById(petId);
		if (petOpt.isEmpty()) {
			log.warn("Mascota no encontrada con ID: {}", petId);
			throw new NotFoundException("No se ha encontrado ninguna mascota con ese identificador.");
		}
		return petOpt.get();
	}

	/**
	 * Verifica que la mascota indicada pertenezca al cliente autenticado. Valida
	 * tanto existencia como propiedad.
	 *
	 * @param repo   Repositorio de mascotas
	 * @param petId  ID de la mascota
	 * @param client Cliente autenticado
	 * @return Mascota si pertenece al cliente
	 * @throws NotFoundException      si no se encuentra la mascota
	 * @throws UnauthorizedException si no pertenece al cliente
	 */
	public static Pet validatePetOwner(PetRepository repo, Long petId, Client client) {
		Pet pet = validatePetExists(repo, petId);
		if (!pet.getClient().getId().equals(client.getId())) {
			log.warn("Cliente ID {} intentó acceder/modificar mascota ajena (pertenece a: {}).", client.getId(),
					pet.getClient().getId());
			throw new UnauthorizedException("No tienes permiso para modificar esta mascota.");
		}
		return pet;
	}

	// ╔════════════════════════════════════════════════════════════╗
	// ║ VALIDACIONES DE NEGOCIO 									║
	// ╚════════════════════════════════════════════════════════════╝

	/**
	 * Valida reglas de negocio adicionales para el registro de mascotas.
	 * <p>
	 * Comprueba campos dependientes como:
	 * <ul>
	 * <li>Si el tipo es OTRO, debe indicarse un tipo personalizado</li>
	 * <li>Si se marca como esterilizada, debe indicarse la fecha de
	 * esterilización</li>
	 * </ul>
	 *
	 * @param request DTO de entrada del registro de mascota
	 * @throws BusinessException si hay datos inconsistentes
	 */

	public static void validatePetRequest(PetRequest request) {
		if (request == null) {
			log.warn("Se intentó validar una petición nula de mascota.");
			throw new BusinessException("La solicitud de registro de mascota no puede estar vacía.");
		}

		if (request.getType() == PetType.OTRO) {
			String customType = request.getCustomType();
			if (customType == null || customType.trim().isEmpty()) {
				log.warn("Mascota con tipo 'OTRO' sin tipo personalizado informado.");
				throw new BusinessException("Si seleccionas tipo 'Otro', debes especificar el tipo personalizado.");
			}
		}
		
		if (request.getPetGender() == PetGender.OTRO) {
		    String customGender = request.getCustomGender();
		    if (customGender == null || customGender.trim().isEmpty()) {
		        log.warn("Mascota con género 'OTRO' sin género personalizado informado.");
		        throw new BusinessException("Si seleccionas género 'Otro', debes especificar el género personalizado.");
		    }
		}

		if (Boolean.TRUE.equals(request.getSterilized()) && request.getSterilizationDate() == null) {
			log.warn("Mascota marcada como esterilizada pero sin fecha de esterilización.");
			throw new BusinessException("Si marcas como esterilizado/a, debes indicar la fecha de esterilización.");
		}

		log.info("Validación de reglas de negocio superada para el registro de mascota.");
	}

	/**
	 * Valida reglas de negocio aplicables al proceso de actualización de una mascota.
	 * <p>
	 * Aunque todos los campos son opcionales, existen dependencias lógicas que deben cumplirse:
	 * <ul>
	 *   <li>Si se marca como esterilizada, debe indicarse la fecha de esterilización</li>
	 *   <li>Si se selecciona tipo "OTRO", debe especificarse el tipo personalizado</li>
	 *   <li>Si se selecciona género "OTRO", debe especificarse el género personalizado</li>
	 * </ul>
	 * Estas validaciones aseguran la coherencia de los datos antes de actualizar la mascota.
	 *
	 * @param update DTO con los nuevos datos de la mascota
	 * @throws BusinessException si alguna combinación de datos es inconsistente
	 */
	
	public static void validatePetUpdate(PetUpdate update) {
		if (update == null) {
			log.warn("Se intentó validar una actualización nula de mascota.");
			throw new BusinessException("La solicitud de actualización de mascota no puede estar vacía.");
		}

		if (update.getType() == PetType.OTRO) {
			String customType = update.getCustomType();
			if (customType == null || customType.trim().isEmpty()) {
				log.warn("Actualización con tipo 'OTRO' pero sin tipo personalizado.");
				throw new BusinessException("Si seleccionas tipo 'Otro', debes especificar el tipo personalizado.");
			}
		}

		if (update.getPetGender() == PetGender.OTRO) {
			String customGender = update.getCustomGender();
			if (customGender == null || customGender.trim().isEmpty()) {
				log.warn("Actualización con género 'OTRO' pero sin género personalizado.");
				throw new BusinessException("Si seleccionas género 'Otro', debes especificar el género personalizado.");
			}
		}

		if (Boolean.TRUE.equals(update.getSterilized()) && update.getSterilizationDate() == null) {
			log.warn("Mascota marcada como esterilizada pero sin fecha de esterilización.");
			throw new BusinessException("Si marcas como esterilizado/a, debes indicar la fecha de esterilización.");
		}

		log.info("Validación de reglas de negocio superada para la actualización de mascota.");
	}
}
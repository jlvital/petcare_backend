package com.petcare.validators;

import com.petcare.domain.client.Client;
import com.petcare.domain.pet.Pet;
import com.petcare.domain.pet.PetRepository;
import com.petcare.domain.pet.dto.PetRequest;
import com.petcare.domain.pet.dto.PetUpdate;
import com.petcare.enums.PetType;
import com.petcare.exceptions.*;

import lombok.extern.slf4j.Slf4j;

/**
 * Clase de utilidades para validar operaciones relacionadas con mascotas.
 * <p>
 * Incluye validaciones de:
 * - Existencia de mascota
 * - Propiedad de una mascota por parte de un cliente
 * - Reglas de negocio para creación y modificación
 *
 * @author Jose
 */
@Slf4j
public class PetValidator {

    // ╔════════════════════════════════════════════════════════════╗
    // ║ EXISTENCIA Y PROPIEDAD 									║
    // ╚════════════════════════════════════════════════════════════╝

    /**
     * Busca una mascota por su ID en el repositorio.
     *
     * @param repo Repositorio de mascotas.
     * @param petId Identificador de la mascota.
     * @return Mascota encontrada.
     * @throws BusinessException si el repositorio o ID son nulos.
     * @throws NotFoundException si no se encuentra la mascota.
     */
    public static Pet findPet(PetRepository repo, Long petId) {
        if (repo == null || petId == null) {
            log.warn("Repositorio o ID de mascota nulo.");
            throw new BusinessException("Error interno al buscar la mascota.");
        }
        return repo.findById(petId).orElseThrow(() -> {
            log.warn("Mascota no encontrada con ID: {}", petId);
            return new NotFoundException("No se ha encontrado ninguna mascota con ese identificador.");
        });
    }

    /**
     * Verifica si la mascota pertenece al cliente especificado.
     *
     * @param pet Mascota a validar.
     * @param clientId ID del cliente autenticado.
     * @throws UnauthorizedException si no coincide el propietario.
     */
    public static void checkOwner(Pet pet, Long clientId) {
        if (pet == null || pet.getClient() == null || clientId == null) {
            log.warn("Fallo al verificar propietario: datos nulos.");
            throw new UnauthorizedException("No tienes acceso a esta mascota.");
        }
        if (!pet.getClient().getId().equals(clientId)) {
            log.warn("Cliente ID [{}] intentó acceder a mascota ID [{}] que pertenece a cliente ID [{}]",
                    clientId, pet.getId(), pet.getClient().getId());
            throw new UnauthorizedException("No tienes permiso para acceder a esta mascota.");
        }
        log.info("Propiedad confirmada: mascota ID [{}] pertenece a cliente ID [{}]", pet.getId(), clientId);
    }

    /**
     * Valida que un cliente tenga acceso a una mascota concreta,
     * incluyendo su existencia y propiedad.
     *
     * @param repo Repositorio de mascotas.
     * @param petId ID de la mascota.
     * @param client Cliente autenticado.
     * @return Mascota validada.
     */
    public static Pet validatePetAccess(PetRepository repo, Long petId, Client client) {
        Pet pet = findPet(repo, petId);
        checkOwner(pet, client.getId());
        return pet;
    }

    // ╔════════════════════════════════════════════════════════════╗
    // ║ VALIDACIONES DE NEGOCIO: REGISTRO Y ACTUALIZACIÓN			║
    // ╚════════════════════════════════════════════════════════════╝

    /**
     * Valida los campos requeridos para registrar una nueva mascota.
     *
     * @param request Solicitud de registro.
     * @throws BusinessException si algún campo obligatorio no es válido.
     */
    public static void validatePetRequest(PetRequest request) {
        if (request == null) {
            log.warn("Petición nula al registrar mascota.");
            throw new BusinessException("La solicitud de registro no puede estar vacía.");
        }

        if (request.getType() == PetType.OTRO) {
            String customType = request.getCustomType();
            if (customType == null || customType.trim().isEmpty()) {
                log.warn("Mascota tipo OTRO sin tipo personalizado.");
                throw new BusinessException("Si seleccionas tipo 'Otro', debes indicar el tipo personalizado.");
            }
        }

        if (Boolean.TRUE.equals(request.getSterilized()) && request.getSterilizationDate() == null) {
            log.warn("Mascota marcada esterilizada pero sin fecha.");
            throw new BusinessException("Debes indicar la fecha si está esterilizado/a.");
        }

        log.info("Validación de registro OK.");
    }

    /**
     * Valida los campos requeridos para actualizar una mascota existente.
     *
     * @param update Solicitud de actualización parcial.
     * @throws BusinessException si algún campo obligatorio no es válido.
     */
    public static void validatePetUpdate(PetUpdate update) {
        if (update == null) {
            log.warn("Actualización nula de mascota.");
            throw new BusinessException("La solicitud de modificación no puede estar vacía.");
        }

        if (update.getType() == PetType.OTRO) {
            String customType = update.getCustomType();
            if (customType == null || customType.trim().isEmpty()) {
                log.warn("Actualización con tipo OTRO pero sin tipo personalizado.");
                throw new BusinessException("Si seleccionas tipo 'Otro', debes indicar el tipo personalizado.");
            }
        }

        if (Boolean.TRUE.equals(update.getSterilized()) && update.getSterilizationDate() == null) {
            log.warn("Mascota marcada esterilizada pero sin fecha.");
            throw new BusinessException("Debes indicar la fecha si está esterilizado/a.");
        }

        log.info("Validación de actualización OK.");
    }
}

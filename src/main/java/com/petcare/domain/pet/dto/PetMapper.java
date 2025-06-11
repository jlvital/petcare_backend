package com.petcare.domain.pet.dto;

import com.petcare.domain.client.Client;
import com.petcare.domain.pet.Pet;

/**
 * Clase utilitaria para convertir entre entidades {@link Pet} y sus DTOs
 * asociados.
 * <p>
 * Se utiliza para mapear datos en las operaciones de:
 * <ul>
 * <li>Registro de mascotas</li>
 * <li>Actualización/Edición</li>
 * <li>Respuesta al cliente</li>
 * </ul>
 * Esta clase no se instancia.
 */
public class PetMapper {

	/**
	 * Convierte una entidad {@link Pet} en su representación {@link PetResponse}.
	 * <p>
	 * Incluye lógica para mostrar correctamente el género personalizado si se
	 * eligió "OTRO".
	 *
	 * @param pet Entidad de mascota que se desea mapear.
	 * @return Objeto {@link PetResponse} con los datos visibles al cliente, o
	 *         {@code null} si la entrada es nula.
	 */

	public static PetResponse toResponse(Pet pet) {
		if (pet == null) {
			return null;
		}

		String genderLabel = null;

		if (pet.getPetGender() != null) {
			genderLabel = pet.getPetGender().getLabel();
		}

		return PetResponse.builder().id(pet.getId()).name(pet.getName()).petGender(pet.getPetGender())
				.genderLabel(genderLabel).customGender(pet.getCustomGender()).type(pet.getType())
				.customType(pet.getCustomType()).chipNumber(pet.getChipNumber()).breed(pet.getBreed())
				.birthDate(pet.getBirthDate()).adoptionDate(pet.getAdoptionDate()).weight(pet.getWeight())
				.sterilized(pet.getSterilized()).sterilizationDate(pet.getSterilizationDate())
				.observations(pet.getObservations()).build();
	}

	/**
	 * Convierte un objeto {@link PetRequest} (formulario de entrada) en una entidad
	 * {@link Pet}.
	 * <p>
	 * Se utiliza durante el proceso de registro, asociando también al cliente
	 * propietario.
	 *
	 * @param request Datos del formulario de registro de mascota.
	 * @param client  Cliente autenticado que registra la mascota.
	 * @return Entidad {@link Pet} lista para persistir, o {@code null} si algún
	 *         dato clave es nulo.
	 */
	public static Pet toEntity(PetRequest request, Client client) {
		if (request == null || client == null) {
			return null;
		}

		Pet pet = new Pet();
		pet.setName(request.getName());
		pet.setPetGender(request.getPetGender());
		pet.setType(request.getType());
		pet.setCustomType(request.getCustomType());
		pet.setChipNumber(request.getChipNumber());
		pet.setBreed(request.getBreed());
		pet.setBirthDate(request.getBirthDate());
		pet.setAdoptionDate(request.getAdoptionDate());
		pet.setWeight(request.getWeight());
		pet.setSterilized(request.getSterilized());
		pet.setSterilizationDate(request.getSterilizationDate());
		pet.setObservations(request.getObservations());
		pet.setClient(client);

		return pet;
	}

	/**
	 * Aplica los cambios contenidos en un objeto {@link PetUpdate} a una entidad
	 * {@link Pet}.
	 * <p>
	 * Solo se modifican aquellos campos que se han informado (no nulos). Este
	 * método se utiliza para actualizaciones desde el área de cliente.
	 *
	 * @param request DTO con los datos nuevos (puede tener campos nulos).
	 * @param pet     Entidad existente que será modificada.
	 */
	public static void updateEntityFromRequest(PetUpdate request, Pet pet) {
		if (request != null && pet != null) {

			if (request.getName() != null) {
				pet.setName(request.getName());
			}

			if (request.getPetGender() != null) {
				pet.setPetGender(request.getPetGender());
			}

			if (request.getType() != null) {
				pet.setType(request.getType());
			}

			if (request.getCustomType() != null) {
				pet.setCustomType(request.getCustomType());
			}

			if (request.getBreed() != null) {
				pet.setBreed(request.getBreed());
			}

			if (request.getBirthDate() != null) {
				pet.setBirthDate(request.getBirthDate());
			}

			if (request.getAdoptionDate() != null) {
				pet.setAdoptionDate(request.getAdoptionDate());
			}

			if (request.getWeight() != null) {
				pet.setWeight(request.getWeight());
			}

			if (request.getSterilized() != null) {
				pet.setSterilized(request.getSterilized());
			}

			if (request.getSterilizationDate() != null) {
				pet.setSterilizationDate(request.getSterilizationDate());
			}

			if (request.getObservations() != null) {
				pet.setObservations(request.getObservations());
			}
		}
	}
}
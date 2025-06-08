package com.petcare.domain.pet;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.petcare.domain.client.Client;
import com.petcare.domain.pet.dto.PetMapper;
import com.petcare.domain.pet.dto.PetRequest;
import com.petcare.domain.pet.dto.PetResponse;
import com.petcare.domain.pet.dto.PetUpdate;
import com.petcare.validators.AccountValidator;
import com.petcare.validators.ClientValidator;
import com.petcare.validators.PetValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;

    // ╔════════════════════════════════════════════════════════════╗
    // ║       REGISTRO DE MASCOTAS                                ║
    // ╚════════════════════════════════════════════════════════════╝

    /**
     * Registra una nueva mascota para el cliente autenticado.
     * <p>
     * Se validan:
     * - El estado de la cuenta del cliente
     * - Su autenticación
     * - Las reglas de negocio dependientes entre campos del formulario
     *
     * @param request Datos del formulario de registro de mascota.
     * @param client Cliente autenticado.
     * @return Mascota registrada, convertida en objeto de respuesta.
     */
    
    @Override
    public PetResponse registerPet(PetRequest request, Client client) {
        AccountValidator.validateAccountIsActive(client);
        ClientValidator.validateAuthenticatedClient(client);
        PetValidator.validatePetRequest(request); 

        Pet pet = PetMapper.toEntity(request, client);
        Pet saved = petRepository.save(pet);

        log.info("Mascota registrada: ID {} | Nombre: {} | Cliente ID {}", saved.getId(), saved.getName(), client.getId());
        return PetMapper.toResponse(saved);
    }

    // ╔════════════════════════════════════════════════════════════╗
    // ║       ACTUALIZACIÓN DE MASCOTAS                           ║
    // ╚════════════════════════════════════════════════════════════╝

    /**
     * Actualiza parcialmente una mascota si pertenece al cliente autenticado.
     * <p>
     * Se permiten modificaciones en los campos no nulos enviados por el cliente.
     * Antes de aplicar los cambios, se realizan varias validaciones:
     * <ul>
     *   <li>Que el cliente esté autenticado y tenga la cuenta activa</li>
     *   <li>Que la mascota exista y pertenezca al cliente</li>
     *   <li>Que los nuevos datos sean coherentes según las reglas del negocio (por ejemplo: tipo personalizado o esterilización)</li>
     * </ul>
     * Solo se modifican los campos informados. El resto permanece sin cambios.
     *
     * @param petId ID de la mascota a modificar
     * @param request datos nuevos de la mascota
     * @param client cliente autenticado
     * @return mascota actualizada
     * @throws BusinessException si alguno de los datos no es válido o no cumple las reglas
     */

    @Override
    public PetResponse updatePet(Long petId, PetUpdate request, Client client) {
        AccountValidator.validateAccountIsActive(client);
        ClientValidator.validateAuthenticatedClient(client);

        Pet pet = PetValidator.validatePetOwner(petRepository, petId, client);
        PetValidator.validatePetUpdate(request);
        
        PetMapper.updateEntityFromRequest(request, pet);
        Pet updated = petRepository.save(pet);

        log.info("Mascota ID {} actualizada correctamente", updated.getId());
        return PetMapper.toResponse(updated);
    }

    // ╔════════════════════════════════════════════════════════════╗
    // ║       CONSULTA DE MASCOTAS DEL CLIENTE                    ║
    // ╚════════════════════════════════════════════════════════════╝

    /**
     * Devuelve todas las mascotas registradas por un cliente concreto.
     *
     * @param clientId ID del cliente
     * @return lista de mascotas
     */
    @Override
    public List<PetResponse> getPetsByClient(Long clientId) {
        List<Pet> pets = petRepository.findByClientId(clientId);
        List<PetResponse> responses = new ArrayList<>();
        for (Pet pet : pets) {
            responses.add(PetMapper.toResponse(pet));
        }
        return responses;
    }

    // ╔════════════════════════════════════════════════════════════╗
    // ║       CONSULTA DE UNA MASCOTA POR ID                      ║
    // ╚════════════════════════════════════════════════════════════╝

    /**
     * Devuelve los datos de una mascota a partir de su ID.
     * Se lanza excepción si no existe.
     *
     * @param petId ID de la mascota
     * @return mascota encontrada
     */
    @Override
    public PetResponse getPetById(Long petId) {
        Pet pet = PetValidator.validatePetExists(petRepository, petId);
        return PetMapper.toResponse(pet);
    }
}
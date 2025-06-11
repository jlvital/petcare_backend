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

    @Override
    public PetResponse updatePet(Long petId, PetUpdate request, Client client) {
        AccountValidator.validateAccountIsActive(client);
        ClientValidator.validateAuthenticatedClient(client);

        Pet pet = PetValidator.validatePetAccess(petRepository, petId, client); // <--- NUEVO
        PetValidator.validatePetUpdate(request);

        PetMapper.updateEntityFromRequest(request, pet);
        Pet updated = petRepository.save(pet);

        log.info("Mascota ID {} actualizada correctamente", updated.getId());
        return PetMapper.toResponse(updated);
    }

    // ╔════════════════════════════════════════════════════════════╗
    // ║       CONSULTA DE MASCOTAS DEL CLIENTE                    ║
    // ╚════════════════════════════════════════════════════════════╝

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

    @Override
    public PetResponse getPetById(Long petId) {
        Pet pet = PetValidator.findPet(petRepository, petId); // reutiliza validación básica
        return PetMapper.toResponse(pet);
    }

    // ╔════════════════════════════════════════════════════════════╗
    // ║       ELIMINACIÓN DE MASCOTAS                             ║
    // ╚════════════════════════════════════════════════════════════╝

    @Override
    public void deletePet(Long petId, Client client) {
        Pet pet = PetValidator.validatePetAccess(petRepository, petId, client); // <--- NUEVO
        petRepository.delete(pet);
        log.info("Mascota ID {} eliminada por cliente ID {}", petId, client.getId());
    }
}

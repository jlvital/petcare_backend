package com.petcare.domain.pet;

import java.util.List;

import com.petcare.domain.client.Client;
import com.petcare.domain.pet.dto.PetRequest;
import com.petcare.domain.pet.dto.PetResponse;
import com.petcare.domain.pet.dto.PetUpdate;

public interface PetService {

    PetResponse registerPet(PetRequest request, Client client);

    PetResponse updatePet(Long petId, PetUpdate request, Client client);

    List<PetResponse> getPetsByClient(Long clientId);

    PetResponse getPetById(Long petId);
    
    void deletePet(Long petId, Client client);

}
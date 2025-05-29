package com.petcare.model.client;

import java.util.List;

import com.petcare.model.client.dto.ClientRegisterRequest;
import com.petcare.model.client.dto.ClientUpdateRequest;
import com.petcare.model.pet.Pet;

public interface ClientService {

	List<Pet> getPetsOfClient(Client client);

	Client registerClient(ClientRegisterRequest request);

	void updateClientProfile(Client client, ClientUpdateRequest request);

	// Client findClientById(Long id);
	Pet registerPet(Client client, Pet pet);

	void deleteClientAccount(Client client);
}
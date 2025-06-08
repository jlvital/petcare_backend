package com.petcare.domain.client;

import java.util.List;

import com.petcare.domain.client.dto.ClientRequest;
import com.petcare.domain.client.dto.ClientUpdate;
import com.petcare.domain.pet.Pet;

public interface ClientService {

	List<Pet> getPetsOfClient(Client client);

	Client registerClient(ClientRequest request);

	void updateClientProfile(Client client, ClientUpdate request);

	Client findClientById(Long id);

	Pet registerPet(Client client, Pet pet);

	void deleteClientAccount(Client client);
}
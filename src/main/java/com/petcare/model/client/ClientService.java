package com.petcare.model.client;

import java.util.List;

import com.petcare.model.client.dto.ClientRegistrationRequest;
import com.petcare.model.pet.Pet;
public interface ClientService {

	List<Pet> getPetsOfClient(Client client);

	Client registerClient(ClientRegistrationRequest request);

	//Client findClientById(Long id);

	Pet registerPet(Client client, Pet pet);

	void deleteClientAccount(Client client);

}
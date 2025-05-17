package com.petcare.model.client;

import java.util.Collections;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.petcare.enums.Role;
import com.petcare.exceptions.UserAlreadyExistsException;
import com.petcare.model.client.dto.ClientRegistrationRequest;
import com.petcare.model.pet.Pet;
import com.petcare.model.pet.PetRepository;
import com.petcare.model.user.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final UserRepository userRepository; // sustituye a ClientRepository
    private final PasswordEncoder passwordEncoder;
    private final PetRepository petRepository;

    @Override
    @Transactional
    public Client registerClient(ClientRegistrationRequest request) {
        if (request == null || request.getUsername() == null) {
            throw new IllegalArgumentException("Debes indicar un nombre de usuario válido");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            log.warn("Intento de registro con email ya existente: {}", request.getUsername());
            throw new UserAlreadyExistsException("Ya existe un usuario con email: " + request.getUsername());
        }

        Client client = new Client();
        client.setName(request.getName());
        client.setUsername(request.getUsername());
        client.setPassword(passwordEncoder.encode(request.getPassword()));
        client.setRole(Role.CLIENTE);
        client.setRecoveryEmail(client.getUsername());

        Client saved = userRepository.save(client); // sigue funcionando por ser subclase de User
        log.info("Cliente registrado correctamente con ID: {}", saved.getId());
        return saved;
    }

    @Override
    public List<Pet> getPetsOfClient(Client client) {
        if (client == null || client.getId() == null) {
            log.warn("Error al recuperar mascotas: cliente nulo o con ID no válido..");
            return Collections.emptyList();
        }

        List<Pet> pets = petRepository.findByClientId(client.getId());

        if (pets.isEmpty()) {
            log.info("No se encontraron mascotas registradas para el cliente con ID: {}", client.getId());
        } else {
            log.info("Se encontraron {} mascotas para el cliente con ID: {}", pets.size(), client.getId());
        }

        return pets;
    }

    @Override
    @Transactional
    public Pet registerPet(Client client, Pet pet) {
        if (client == null || client.getId() == null) {
            log.warn("Error al agregar mascota: cliente nulo o con ID no válido.");
            throw new IllegalArgumentException("Cliente inválido para registrar mascota.");
        }

        if (pet == null) {
            log.warn("Error al agregar mascota: el objeto Pet es nulo.");
            throw new IllegalArgumentException("Los datos de la mascota no pueden ser nulos.");
        }

        pet.setClient(client);
        Pet savedPet = petRepository.save(pet);
        log.info("Mascota '{}' registrada correctamente para el cliente con ID: {}", savedPet.getName(), client.getId());
        return savedPet;
    }

    @Override
    @Transactional
    public void deleteClientAccount(Client client) {
        if (client == null || client.getId() == null) {
            log.warn("No se puede eliminar cuenta: cliente nulo o sin ID.");
            throw new IllegalArgumentException("Cliente inválido para eliminación.");
        }

        List<Pet> mascotas = petRepository.findByClientId(client.getId());
        for (Pet pet : mascotas) {
            petRepository.delete(pet);
        }

        userRepository.delete(client);
        log.info("Cuenta eliminada para cliente con username: {}", client.getUsername());
    }
}

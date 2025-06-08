package com.petcare.domain.client;

import java.util.Collections;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.petcare.domain.client.dto.ClientMapper;
import com.petcare.domain.client.dto.ClientRequest;
import com.petcare.domain.client.dto.ClientUpdate;
import com.petcare.domain.pet.Pet;
import com.petcare.domain.pet.PetRepository;
import com.petcare.domain.user.UserRepository;
import com.petcare.enums.Role;
import com.petcare.notification.SystemEmailService;
import com.petcare.validators.ClientValidator;
import com.petcare.validators.UserValidator;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PetRepository petRepository;
    private final SystemEmailService systemEmailService;


    @Override
    public Client findClientById(Long id) {
        return ClientValidator.requireClientById(userRepository, id);
    }

    // ═══════════════════════════════════════════════════════════════
    // REGISTRO DE CLIENTE
    // ═══════════════════════════════════════════════════════════════
    @Override
    @Transactional
    public Client registerClient(ClientRequest request) {
        if (request == null || request.getUsername() == null) {
            throw new IllegalArgumentException("Debes indicar un nombre de usuario válido");
        }

        ClientValidator.validateRegisterRequest(request, userRepository);

        Client client = ClientMapper.toEntity(request, passwordEncoder);
        client.setRecoveryEmail(request.getUsername());

        UserValidator.validateUsernameAndRecoveryEmail(client);

        Client saved = userRepository.save(client);
        log.info("Cliente registrado correctamente con ID: {}", saved.getId());

        systemEmailService.sendWelcomeEmail(
        	    saved.getUsername(),
        	    Role.CLIENTE.getLabel(),
        	    saved.getName(),
        	    request.getPassword()
        	);

        return saved;
    }

    // ═══════════════════════════════════════════════════════════════
    // ACTUALIZACIÓN DEL PERFIL DEL CLIENTE
    // ═══════════════════════════════════════════════════════════════
    @Override
    @Transactional
    public void updateClientProfile(Client client, ClientUpdate request) {
        if (client == null || client.getId() == null) {
            log.warn("Intento de actualización con cliente nulo o sin ID.");
            throw new IllegalArgumentException("Cliente inválido para actualizar perfil.");
        }

        if (request == null) {
            log.warn("Datos de actualización nulos para el cliente ID {}", client.getId());
            throw new IllegalArgumentException("Los datos de actualización no pueden ser nulos.");
        }

        ClientMapper.updateEntityFromRequest(request, client);
        userRepository.save(client);

        log.info("Perfil del cliente con ID {} actualizado correctamente.", client.getId());
    }

    // ═══════════════════════════════════════════════════════════════
    // MASCOTAS DEL CLIENTE
    // ═══════════════════════════════════════════════════════════════
    @Override
    public List<Pet> getPetsOfClient(Client client) {
        if (client == null || client.getId() == null) {
            log.warn("Error al recuperar mascotas: cliente nulo o con ID no válido.");
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

    // ═══════════════════════════════════════════════════════════════
    // REGISTRO DE MASCOTA
    // ═══════════════════════════════════════════════════════════════
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

    // ═══════════════════════════════════════════════════════════════
    // ELIMINACIÓN DE CUENTA DE CLIENTE
    // ═══════════════════════════════════════════════════════════════
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
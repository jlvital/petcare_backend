package com.petcare.model.vaccine;

import com.petcare.model.pet.Pet;
import com.petcare.model.pet.PetRepository;
import com.petcare.model.vaccine.dto.VaccineResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VaccineServiceImpl implements VaccineService {

    private final VaccineRepository vaccineRepository;
    private final PetRepository petRepository;

    @Override
    @Transactional
    public List<VaccineResponse> getVaccinesByPet(Long petId, Long clientId) {
        Pet pet = petRepository.findById(petId).orElseThrow(() ->
                new IllegalArgumentException("Mascota no encontrada con ID: " + petId));

        if (!pet.getClient().getId().equals(clientId)) {
            log.warn("⛔ Cliente ID {} intentó acceder a vacunas de mascota ajena ID {}.", clientId, petId);
            throw new IllegalArgumentException("No tienes permiso para ver las vacunas de esta mascota.");
        }

        List<VaccineResponse> responses = new ArrayList<>();
        for (Vaccine vaccine : vaccineRepository.findByPetId(petId)) {
            responses.add(VaccineResponse.fromEntity(vaccine));
        }

        log.info("💉 Se encontraron {} vacunas para la mascota ID {} del cliente ID {}.", responses.size(), petId, clientId);
        return responses;
    }
}
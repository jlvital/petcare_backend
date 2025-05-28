package com.petcare.model.treatment;

import com.petcare.model.treatment.dto.TreatmentResponse;
import com.petcare.model.pet.Pet;
import com.petcare.model.pet.PetRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TreatmentServiceImpl implements TreatmentService {

    private final TreatmentRepository treatmentRepository;
    private final PetRepository petRepository;

    @Override
    @Transactional
    public List<TreatmentResponse> getTreatmentsByPet(Long petId, Long clientId) {
        Pet pet = petRepository.findById(petId).orElseThrow(() ->
                new IllegalArgumentException("Mascota no encontrada con ID: " + petId));

        if (!pet.getClient().getId().equals(clientId)) {
            log.warn("Acceso no autorizado: cliente ID {} intentó acceder a tratamientos de la mascota ID {}.", clientId, petId);
            throw new IllegalArgumentException("No tienes acceso a los tratamientos de esta mascota.");
        }

        List<TreatmentResponse> responses = new ArrayList<>();
        for (Treatment treatment : treatmentRepository.findByPetId(petId)) {
            responses.add(TreatmentResponse.fromEntity(treatment));
        }

        log.info("Se recuperaron {} tratamientos para la mascota ID {} del cliente ID {}", responses.size(), petId, clientId);
        return responses;
    }
}
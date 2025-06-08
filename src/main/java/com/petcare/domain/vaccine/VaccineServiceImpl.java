package com.petcare.domain.vaccine;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.petcare.domain.pet.Pet;
import com.petcare.domain.pet.PetRepository;
import com.petcare.domain.vaccine.dto.LatestVaccineResponse;
import com.petcare.domain.vaccine.dto.VaccineMapper;
import com.petcare.domain.vaccine.dto.VaccineRequest;
import com.petcare.domain.vaccine.dto.VaccineResponse;
import com.petcare.domain.vaccine.dto.VaccineUpdate;
import com.petcare.exceptions.*;
import com.petcare.validators.AccountValidator;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VaccineServiceImpl implements VaccineService {

    private final VaccineRepository vaccineRepository;
    private final PetRepository petRepository;

    @Override
    @Transactional
    public List<VaccineResponse> getVaccinesByPet(Long petId, Long clientId) {
        Optional<Pet> optionalPet = petRepository.findById(petId);

        if (optionalPet.isEmpty()) {
            log.warn("Mascota no encontrada con ID: {}", petId);
            throw new NotFoundException("No se ha encontrado la mascota con ID: " + petId);
        }

        Pet pet = optionalPet.get();

        if (!pet.getClient().getId().equals(clientId)) {
            log.warn("Acceso denegado: cliente ID {} intentó acceder a la mascota ID {}.", clientId, petId);
            throw new UnauthorizedException("Acceso denegado: esta mascota no pertenece al cliente actual.");
        }

        AccountValidator.validateAccountIsActive(pet.getClient());

        List<Vaccine> vacunas = vaccineRepository.findByPetIdOrderByAdministrationDateDesc(petId);
        List<VaccineResponse> respuestas = new ArrayList<>();

        for (Vaccine vacuna : vacunas) {
            VaccineResponse dto = VaccineMapper.toResponse(vacuna);
            respuestas.add(dto);
        }

        log.info("Se recuperaron {} vacunas para la mascota ID {} del cliente ID {}.",
                 respuestas.size(), petId, clientId);

        return respuestas;
    }

    @Override
    @Transactional
    public void registerVaccine(VaccineRequest request) {
        Optional<Pet> optionalPet = petRepository.findById(request.getPetId());

        if (optionalPet.isEmpty()) {
            log.warn("No se pudo registrar la vacuna. Mascota no encontrada con ID: {}", request.getPetId());
            throw new NotFoundException("No se ha encontrado la mascota con ID: " + request.getPetId());
        }

        Pet pet = optionalPet.get();

        AccountValidator.validateAccountIsActive(pet.getClient());

        Vaccine nuevaVacuna = VaccineMapper.toEntity(request, pet);
        vaccineRepository.save(nuevaVacuna);

        log.info("Vacuna '{}' registrada correctamente para la mascota ID {}.",
                 nuevaVacuna.getName(), pet.getId());
    }

    @Override
    @Transactional
    public void updateVaccine(Long vaccineId, VaccineUpdate request) {
        Optional<Vaccine> optional = vaccineRepository.findById(vaccineId);

        if (optional.isEmpty()) {
            log.warn("Vacuna no encontrada con ID: {}", vaccineId);
            throw new NotFoundException("No se ha encontrado la vacuna con ID: " + vaccineId);
        }

        Vaccine vacuna = optional.get();

        AccountValidator.validateAccountIsActive(vacuna.getPet().getClient());

        VaccineMapper.updateEntityFromRequest(request, vacuna);
        vaccineRepository.save(vacuna);

        log.info("Vacuna actualizada correctamente con ID: {}", vaccineId);
    }

    @Override
    @Transactional
    public LatestVaccineResponse getLatestVaccine(Long petId) {
        List<Vaccine> vacunas = vaccineRepository.findByPetIdOrderByAdministrationDateDesc(petId);

        if (vacunas.isEmpty()) {
            log.warn("No se encontraron vacunas para la mascota ID: {}", petId);
            throw new NotFoundException("La mascota aún no tiene vacunas registradas.");
        }

        Vaccine ultima = vacunas.get(0);

        log.info("Última vacuna encontrada para mascota ID {} con fecha {}", petId, ultima.getAdministrationDate());
        return VaccineMapper.toLatestResponse(ultima);
    }
}
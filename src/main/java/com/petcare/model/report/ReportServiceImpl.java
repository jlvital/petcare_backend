package com.petcare.model.report;

import com.petcare.model.pet.Pet;
import com.petcare.model.pet.PetRepository;
import com.petcare.model.report.dto.ReportResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final PetRepository petRepository;

    @Override
    @Transactional
    public List<ReportResponse> getReportsByPet(Long petId, Long clientId) {
        Pet pet = petRepository.findById(petId).orElseThrow(() ->
                new IllegalArgumentException("Mascota no encontrada con ID: " + petId));

        if (!pet.getClient().getId().equals(clientId)) {
            log.warn("⛔ Acceso denegado: cliente ID {} intentó acceder a informes de mascota ajena ID {}.", clientId, petId);
            throw new IllegalArgumentException("No tienes permiso para acceder a los informes de esta mascota.");
        }

        List<ReportResponse> responses = new ArrayList<>();
        for (Report report : reportRepository.findByPetId(petId)) {
            responses.add(ReportResponse.fromEntity(report));
        }

        log.info("📋 Se recuperaron {} informes para la mascota ID {} (cliente ID {}).", responses.size(), petId, clientId);
        return responses;
    }
}
package com.petcare.domain.report;

import com.petcare.domain.pet.Pet;
import com.petcare.domain.pet.PetRepository;
import com.petcare.domain.report.dto.LatestReportResponse;
import com.petcare.domain.report.dto.ReportMapper;
import com.petcare.domain.report.dto.ReportRequest;
import com.petcare.domain.report.dto.ReportResponse;
import com.petcare.domain.report.dto.ReportUpdate;
import com.petcare.exceptions.*;
import com.petcare.validators.AccountValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j

public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final PetRepository petRepository;

    @Override
    @Transactional
    public List<ReportResponse> getReportsByPet(Long petId, Long clientId) {
        Optional<Pet> optionalPet = petRepository.findById(petId);
        if (optionalPet.isEmpty()) {
            throw new NotFoundException("No se encontró la mascota con ID: " + petId);
        }
        Pet pet = optionalPet.get();

        if (!pet.getClient().getId().equals(clientId)) {
            log.warn("Acceso denegado: cliente ID {} intentó acceder a informes de mascota ajena ID {}.", clientId, petId);
            throw new UnauthorizedException("No tienes permiso para acceder a los informes de esta mascota.");
        }

        AccountValidator.validateAccountIsActive(pet.getClient());
        List<ReportResponse> responses = new ArrayList<>();
        for (Report report : reportRepository.findByPetId(petId)) {
            responses.add(ReportMapper.toResponse(report));
        }

        log.info("Se recuperaron {} informes para la mascota ID {} (cliente ID {}).", responses.size(), petId, clientId);
        return responses;
    }
    
    @Override
    @Transactional
    public void registerReport(ReportRequest request) {
        Optional<Pet> optionalPet = petRepository.findById(request.getPetId());

        if (optionalPet.isEmpty()) {
            log.warn("No se pudo registrar el informe. Mascota no encontrada con ID: {}", request.getPetId());
            throw new NotFoundException("Mascota no encontrada con ID: " + request.getPetId());
        }

        Pet pet = optionalPet.get();

        if (!pet.getClient().getId().equals(request.getClientId())) {
            log.warn("El cliente ID {} no es propietario de la mascota ID {}", request.getClientId(), request.getPetId());
            throw new BusinessException("La mascota no pertenece al cliente indicado.");
        }

        AccountValidator.validateAccountIsActive(pet.getClient());

        Report report = ReportMapper.toEntity(request, pet);

        reportRepository.save(report);
        log.info("Informe médico creado correctamente para mascota ID {} (cliente ID {}).", pet.getId(), pet.getClient().getId());
    }

    @Override
    @Transactional
    public void updateReport(Long reportId, ReportUpdate request) {
        Optional<Report> optionalReport = reportRepository.findById(reportId);

        if (optionalReport.isEmpty()) {
            log.warn("Informe no encontrado con ID: {}", reportId);
            throw new NotFoundException("No se encontró el informe con ID: " + reportId);
        }

        Report report = optionalReport.get();

        AccountValidator.validateAccountIsActive(report.getPet().getClient());
        ReportMapper.updateEntityFromRequest(request, report);

        reportRepository.save(report);
        log.info("Informe actualizado correctamente con ID: {}", reportId);
    }
    
    @Override
    @Transactional
    public LatestReportResponse getLatestReport(Long petId) {
        List<Report> informes = reportRepository.findByPetIdOrderByLastUpdateDesc(petId);

        if (informes.isEmpty()) {
            log.warn("No se encontró ningún informe para la mascota ID: {}", petId);
            throw new UnauthorizedException("La mascota aún no tiene informes registrados.");
        }

        Report latest = informes.get(0);
        AccountValidator.validateAccountIsActive(latest.getPet().getClient());
        log.info("Último informe encontrado para mascota ID {} con fecha {}", petId, latest.getLastUpdate());
        return ReportMapper.toLatestResponse(latest);
    }
}
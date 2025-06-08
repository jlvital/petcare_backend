package com.petcare.domain.treatment;

import com.petcare.domain.pet.Pet;
import com.petcare.domain.pet.PetRepository;
import com.petcare.domain.report.Report;
import com.petcare.domain.report.ReportRepository;
import com.petcare.domain.treatment.dto.*;
import com.petcare.exceptions.*;
import com.petcare.validators.AccountValidator;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TreatmentServiceImpl implements TreatmentService {

    private final TreatmentRepository treatmentRepository;
    private final PetRepository petRepository;
    private final ReportRepository reportRepository;

    @Override
    @Transactional
    public List<TreatmentResponse> getTreatmentsByPet(Long petId, Long clientId) {
        Optional<Pet> optionalPet = petRepository.findById(petId);
        if (optionalPet.isEmpty()) {
            throw new NotFoundException("Mascota no encontrada con ID: " + petId);
        }

        Pet pet = optionalPet.get();

        if (!pet.getClient().getId().equals(clientId)) {
            log.warn("Acceso no autorizado: cliente ID {} intentó acceder a tratamientos de la mascota ID {}.",
                    clientId, petId);
            throw new UnauthorizedException("No tienes acceso a los tratamientos de esta mascota.");
        }

        AccountValidator.validateAccountIsActive(pet.getClient());

        List<TreatmentResponse> responses = new ArrayList<>();
        for (Treatment treatment : treatmentRepository.findByPetId(petId)) {
            responses.add(TreatmentMapper.toResponse(treatment));
        }

        log.info("Se recuperaron {} tratamientos para la mascota ID {} del cliente ID {}",
                responses.size(), petId, clientId);
        return responses;
    }

    @Override
    @Transactional
    public void registerTreatment(TreatmentRequest request) {
        Optional<Pet> optionalPet = petRepository.findById(request.getPetId());

        if (optionalPet.isEmpty()) {
            log.warn("Mascota no encontrada con ID: {}", request.getPetId());
            throw new NotFoundException("Mascota no encontrada con ID: " + request.getPetId());
        }

        Optional<Report> optionalReport = reportRepository.findById(request.getMedicalReportId());

        if (optionalReport.isEmpty()) {
            log.warn("Informe médico no encontrado con ID: {}", request.getMedicalReportId());
            throw new NotFoundException("Informe médico no encontrado con ID: " + request.getMedicalReportId());
        }

        Pet pet = optionalPet.get();
        Report report = optionalReport.get();

        if (!report.getPet().getId().equals(pet.getId())) {
            log.warn("El informe ID {} no corresponde con la mascota ID {}", report.getId(), pet.getId());
            throw new BusinessException("El informe no pertenece a la mascota indicada.");
        }

        AccountValidator.validateAccountIsActive(pet.getClient()); // ✅ Validación de cuenta activa

        Treatment treatment = TreatmentMapper.toEntity(request, pet, report);

        treatmentRepository.save(treatment);
        log.info("Tratamiento creado correctamente para mascota ID {} e informe ID {}.",
                pet.getId(), report.getId());
    }

    @Override
    @Transactional
    public void updateTreatment(Long treatmentId, TreatmentUpdate request) {
        Optional<Treatment> optional = treatmentRepository.findById(treatmentId);

        if (optional.isEmpty()) {
            log.warn("No se encontró el tratamiento con ID: {}", treatmentId);
            throw new NotFoundException("No se encontró el tratamiento con ID: " + treatmentId);
        }

        Treatment treatment = optional.get();

        AccountValidator.validateAccountIsActive(treatment.getPet().getClient()); 
        TreatmentMapper.updateEntityFromRequest(request, treatment);

        treatmentRepository.save(treatment);
        log.info("Tratamiento actualizado correctamente. ID: {}", treatmentId);
    }
}
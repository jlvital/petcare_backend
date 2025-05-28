package com.petcare.model.treatment;

import com.petcare.model.client.Client;
import com.petcare.model.treatment.dto.TreatmentResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/treatments")
@RequiredArgsConstructor
@Slf4j
public class TreatmentController {

    private final TreatmentService treatmentService;

    @GetMapping("/pet/{petId}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<TreatmentResponse>> getTreatmentsByPet(
            @PathVariable @NotNull Long petId,
            @AuthenticationPrincipal Client client) {

        List<TreatmentResponse> treatments = treatmentService.getTreatmentsByPet(petId, client.getId());
        return treatments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(treatments);
    }
}
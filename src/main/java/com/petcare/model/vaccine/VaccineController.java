package com.petcare.model.vaccine;

import com.petcare.model.client.Client;
import com.petcare.model.vaccine.dto.VaccineResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vaccines")
@RequiredArgsConstructor
@Slf4j
public class VaccineController {

    private final VaccineService vaccineService;

    @GetMapping("/pet/{petId}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<VaccineResponse>> getVaccinesByPet(
            @PathVariable @NotNull Long petId,
            @AuthenticationPrincipal Client client) {

        List<VaccineResponse> vaccines = vaccineService.getVaccinesByPet(petId, client.getId());
        return vaccines.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(vaccines);
    }
}

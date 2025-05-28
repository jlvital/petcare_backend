package com.petcare.model.report;

import com.petcare.model.client.Client;
import com.petcare.model.report.dto.ReportResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@Slf4j
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/pet/{petId}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<ReportResponse>> getReportsByPet(
            @PathVariable @NotNull Long petId,
            @AuthenticationPrincipal Client client) {

        List<ReportResponse> reports = reportService.getReportsByPet(petId, client.getId());
        return reports.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(reports);
    }
}
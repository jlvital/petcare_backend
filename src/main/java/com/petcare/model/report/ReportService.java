package com.petcare.model.report;

import com.petcare.model.report.dto.ReportResponse;
import java.util.List;

public interface ReportService {
    List<ReportResponse> getReportsByPet(Long petId, Long clientId);
}
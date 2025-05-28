package com.petcare.model.treatment;

import com.petcare.model.treatment.dto.TreatmentResponse;

import java.util.List;

public interface TreatmentService {
    List<TreatmentResponse> getTreatmentsByPet(Long petId, Long clientId);
}
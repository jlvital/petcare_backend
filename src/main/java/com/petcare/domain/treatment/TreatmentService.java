package com.petcare.domain.treatment;

import java.util.List;

import com.petcare.domain.treatment.dto.TreatmentRequest;
import com.petcare.domain.treatment.dto.TreatmentResponse;
import com.petcare.domain.treatment.dto.TreatmentUpdate;

public interface TreatmentService {
    List<TreatmentResponse> getTreatmentsByPet(Long petId, Long clientId);

    void registerTreatment(TreatmentRequest request);
    
    void updateTreatment(Long treatmentId, TreatmentUpdate request);
}
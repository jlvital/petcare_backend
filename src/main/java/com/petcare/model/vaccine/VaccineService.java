package com.petcare.model.vaccine;

import com.petcare.model.vaccine.dto.VaccineResponse;

import java.util.List;

public interface VaccineService {
    List<VaccineResponse> getVaccinesByPet(Long petId, Long clientId);
}
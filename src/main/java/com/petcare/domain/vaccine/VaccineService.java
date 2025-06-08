package com.petcare.domain.vaccine;

import java.util.List;

import com.petcare.domain.vaccine.dto.LatestVaccineResponse;
import com.petcare.domain.vaccine.dto.VaccineRequest;
import com.petcare.domain.vaccine.dto.VaccineResponse;
import com.petcare.domain.vaccine.dto.VaccineUpdate;

public interface VaccineService {
	List<VaccineResponse> getVaccinesByPet(Long petId, Long clientId);

	void registerVaccine(VaccineRequest request);

	void updateVaccine(Long vaccineId, VaccineUpdate request);
	
	LatestVaccineResponse getLatestVaccine(Long petId);
}
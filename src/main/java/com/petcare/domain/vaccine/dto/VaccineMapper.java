package com.petcare.domain.vaccine.dto;

import com.petcare.domain.pet.Pet;
import com.petcare.domain.vaccine.Vaccine;

/**
 * Clase auxiliar para convertir objetos VaccineRequest en entidades Vaccine.
 */
public class VaccineMapper {

    /**
     * Convierte un VaccineRequest en una entidad Vaccine.
     *
     * @param request datos del formulario enviados por el usuario
     * @param pet     mascota a la que se le aplica la vacuna
     * @return entidad Vaccine lista para guardar
     */
    public static Vaccine toEntity(VaccineRequest request, Pet pet) {
        if (request == null || pet == null) {
            return null;
        }

        Vaccine vaccine = new Vaccine();
        vaccine.setName(request.getName());
        vaccine.setLab(request.getLab());
        vaccine.setAdministrationDate(request.getAdministrationDate());
        vaccine.setExpirationDate(request.getExpirationDate());
        vaccine.setPurchasePrice(request.getPurchasePrice());
        vaccine.setSalePrice(request.getSalePrice());
        vaccine.setPet(pet);

        return vaccine;
    }
    
    /**
     * Convierte una entidad Vaccine en un DTO de tipo VaccineResponse.
     *
     * @param vaccine entidad persistida en base de datos
     * @return DTO con los campos visibles al cliente
     */
    
    public static VaccineResponse toResponse(Vaccine vaccine) {
        if (vaccine == null) {
            return null;
        }

        return VaccineResponse.builder()
                .id(vaccine.getIdVaccine())
                .name(vaccine.getName())
                .lab(vaccine.getLab())
                .administrationDate(vaccine.getAdministrationDate())
                .expirationDate(vaccine.getExpirationDate())
                .salePrice(vaccine.getSalePrice())
                .purchasePrice(vaccine.getPurchasePrice())
                .petId(vaccine.getPet() != null ? vaccine.getPet().getId() : null)
                .petName(vaccine.getPet() != null ? vaccine.getPet().getName() : null)
                .build();
    }
    
    public static void updateEntityFromRequest(VaccineUpdate request, Vaccine vaccine) {
        if (request.getName() != null) {
            vaccine.setName(request.getName());
        }
        if (request.getLab() != null) {
            vaccine.setLab(request.getLab());
        }
        if (request.getAdministrationDate() != null) {
            vaccine.setAdministrationDate(request.getAdministrationDate());
        }
        if (request.getExpirationDate() != null) {
            vaccine.setExpirationDate(request.getExpirationDate());
        }
        if (request.getPurchasePrice() != null) {
            vaccine.setPurchasePrice(request.getPurchasePrice());
        }
        if (request.getSalePrice() != null) {
            vaccine.setSalePrice(request.getSalePrice());
        }
    }
    public static LatestVaccineResponse toLatestResponse(Vaccine vaccine) {
        if (vaccine == null) {
            return null;
        }

        LatestVaccineResponse dto = new LatestVaccineResponse();
        dto.setVaccineId(vaccine.getIdVaccine());
        dto.setName(vaccine.getName());
        dto.setLab(vaccine.getLab());
        dto.setAdministrationDate(vaccine.getAdministrationDate());
        dto.setExpirationDate(vaccine.getExpirationDate());

        if (vaccine.getPet() != null) {
            dto.setPetName(vaccine.getPet().getName());
        }

        return dto;
    }
}
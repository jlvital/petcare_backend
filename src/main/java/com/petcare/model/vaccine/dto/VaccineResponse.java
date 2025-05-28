package com.petcare.model.vaccine.dto;

import com.petcare.model.vaccine.Vaccine;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VaccineResponse {

    private Long id;
    private String name;
    private String lab;
    private LocalDate administrationDate;
    private LocalDate expirationDate;
    private Double salePrice;

    public static VaccineResponse fromEntity(Vaccine vaccine) {
        return VaccineResponse.builder()
                .id(vaccine.getIdVaccine())
                .name(vaccine.getName())
                .lab(vaccine.getLab())
                .administrationDate(vaccine.getAdministrationDate())
                .expirationDate(vaccine.getExpirationDate())
                .salePrice(vaccine.getSalePrice())
                .build();
    }
}

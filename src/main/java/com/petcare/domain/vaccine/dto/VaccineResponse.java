package com.petcare.domain.vaccine.dto;

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
    private Double purchasePrice;
    private Long petId;
    private String petName;
}
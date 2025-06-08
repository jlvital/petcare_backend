package com.petcare.domain.employee.dto;

import java.time.LocalDate;

import com.petcare.domain.user.dto.UserResponse;
import com.petcare.enums.Profile;
import com.petcare.enums.UserGender;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class EmployeeResponse extends UserResponse {

    private LocalDate startDate;
    private LocalDate endDate;

    private Profile profile;
    private String profileLabel;
    
    private UserGender gender;
    private String genderLabel;

    private String roleLabel;
    private String accountStatusLabel;
}
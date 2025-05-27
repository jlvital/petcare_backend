package com.petcare.model.employee.dto;

import java.time.LocalDate;

import com.petcare.model.user.dto.UserResponse;
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
}
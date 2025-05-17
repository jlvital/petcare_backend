package com.petcare.model.appointment.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.petcare.enums.AppointmentType;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class AppointmentRequest {
    @NotNull private LocalDate date;
    @NotNull private LocalTime time;
    @NotNull private AppointmentType type;
    @NotNull private Long petId;
    @NotNull private Long employeeId;
}


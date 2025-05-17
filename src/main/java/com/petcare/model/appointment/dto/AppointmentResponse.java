package com.petcare.model.appointment.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.petcare.enums.AppointmentStatus;
import com.petcare.enums.AppointmentType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AppointmentResponse {
    private Long id;
    private LocalDate date;
    private LocalTime time;
    private AppointmentStatus status;
    private AppointmentType type;
    private String petName;
    private String veterinarianName;
}

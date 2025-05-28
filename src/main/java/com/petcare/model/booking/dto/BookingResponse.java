package com.petcare.model.booking.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.petcare.enums.BookingStatus;
import com.petcare.enums.BookingType;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookingResponse {

    private Long id;
    private LocalDate date;
    private LocalTime time;
    private BookingStatus status;
    private BookingType type;

    private String petName;
    private String employeeName;

    private String clientName;
    private String petType;
    
    private Long petId;
    private Long employeeId;
    private Integer durationMinutes;
    private String employeeProfile;
}
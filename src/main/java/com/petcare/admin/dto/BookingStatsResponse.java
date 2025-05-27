package com.petcare.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class BookingStatsResponse {

    private int totalBookings;
    private int confirmedBookings;
    private int cancelledBookings;
    private int abortedBookings;

    private int totalClients;
    private int totalEmployees;
}
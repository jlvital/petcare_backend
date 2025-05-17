package com.petcare.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AdminStatsResponse {

    private int totalAppointments;
    private int confirmedAppointments;
    private int cancelledAppointments;

    private int totalClients;
    private int totalEmployees;
}
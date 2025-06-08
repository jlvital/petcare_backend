package com.petcare.admin.dto;

import java.util.Map;

import com.petcare.enums.BookingType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para representar estadísticas de uso de los distintos servicios (tipos de cita).
 * Permite mostrar totales y porcentajes por tipo, así como destacar los más y menos demandados.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminServiceStats {

    private int totalBookings;

    private BookingType mostDemandedService;
    private BookingType leastDemandedService;

    private Map<BookingType, Integer> totalByService;
    private Map<BookingType, Double> percentageByService;

    private String mostDemandedServiceLabel;
    private String leastDemandedServiceLabel;
    private Map<String, Integer> totalByServiceLabel;
    private Map<String, Double> percentageByServiceLabel;
}
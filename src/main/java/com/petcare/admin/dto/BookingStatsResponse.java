package com.petcare.admin.dto;

import java.util.HashMap;
import java.util.Map;

import com.petcare.enums.BookingStatus;
import com.petcare.enums.BookingType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para mostrar estadísticas del panel del administrador.
 * Contiene totales y agrupaciones por tipo de cita y estado.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingStatsResponse {

    private int totalBookings;

    private int confirmedBookings;
    private int cancelledBookings;
    private int abortedBookings;
    private int completedBookings; // NUEVO: número de citas atendidas

    private Map<BookingType, Integer> bookingsByType = new HashMap<>();
    private Map<BookingStatus, Integer> bookingsByStatus = new HashMap<>();

    private int totalClients;
    private int totalEmployees;
}
package com.petcare.domain.booking;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.petcare.enums.BookingStatus;
import com.petcare.enums.BookingType;


public interface BookingRepository extends JpaRepository<Booking, Long> {

    // ╔════════════════════════════════════════════════════╗
    // ║     CONSULTAS POR RELACIÓN (cliente, mascota...)   ║
    // ╚════════════════════════════════════════════════════╝
    List<Booking> findByPetId(Long petId);
    List<Booking> findByPetClientId(Long clientId);
    List<Booking> findByEmployeeId(Long employeeId);

    // ╔════════════════════════════════════════════════════╗
    // ║          CONSULTAS POR FECHA Y DISPONIBILIDAD      ║
    // ╚════════════════════════════════════════════════════╝
    boolean existsByEmployeeIdAndDateAndTime(Long employeeId, LocalDate date, LocalTime time);
    List<Booking> findByDate(LocalDate date);
    List<Booking> findByDateAfter(LocalDate date);
    List<Booking> findByDateBetween(LocalDateTime start, LocalDateTime end);
    List<Booking> findByEmployeeIdAndDate(Long employeeId, LocalDate date);
    Page<Booking> findByDateAfter(LocalDate date, Pageable pageable);

    // ╔════════════════════════════════════════════════════╗
    // ║               CONSULTAS POR ATRIBUTOS              ║
    // ╚════════════════════════════════════════════════════╝
    List<Booking> findByStatus(BookingStatus status);
    List<Booking> findByType(BookingType type);
    long countByStatus(BookingStatus status);

    // ╔════════════════════════════════════════════════════╗
    // ║               CONSULTAS PERSONALIZADAS             ║
    // ╚════════════════════════════════════════════════════╝

    /**
     * Devuelve las próximas citas CONFIRMADAS de un cliente.
     * Se ordenan por fecha y hora ascendentes.
     */
    @Query("""
        SELECT b
        FROM Booking b
        WHERE b.pet.client.id = :clientId
          AND b.status = 'CONFIRMADA'
          AND (
              b.date > CURRENT_DATE
              OR (b.date = CURRENT_DATE AND b.time >= CURRENT_TIME)
          )
        ORDER BY b.date ASC, b.time ASC
    """)
    List<Booking> findUpcomingByClientId(Long clientId);

    /**
     * Devuelve las próximas citas CONFIRMADAS de un empleado.
     * Se ordenan por fecha y hora ascendentes.
     */
    @Query("""
        SELECT b
        FROM Booking b
        WHERE b.employee.id = :employeeId
          AND b.status = 'CONFIRMADA'
          AND (
              b.date > CURRENT_DATE
              OR (b.date = CURRENT_DATE AND b.time >= CURRENT_TIME)
          )
        ORDER BY b.date ASC, b.time ASC
    """)
    List<Booking> findUpcomingByEmployeeId(Long employeeId);
}
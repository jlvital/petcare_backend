package com.petcare.model.booking;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petcare.enums.BookingStatus;
import com.petcare.enums.BookingType;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByPetId(Long petId);

    List<Booking> findByEmployeeId(Long employeeId);

    List<Booking> findByStatus(BookingStatus status);

    boolean existsByEmployeeIdAndDateAndTime(Long employeeId, LocalDate date, LocalTime time);

    List<Booking> findByDateBetween(LocalDateTime start, LocalDateTime end);

    List<Booking> findByType(BookingType type);

    long countByStatus(BookingStatus status);

    List<Booking> findByDate(LocalDate date);

    List<Booking> findByEmployeeIdAndDate(Long employeeId, LocalDate date);
}
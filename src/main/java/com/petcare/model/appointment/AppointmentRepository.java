package com.petcare.model.appointment;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petcare.enums.AppointmentStatus;
import com.petcare.enums.AppointmentType;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

	List<Appointment> findByPetId(Long petId);

	List<Appointment> findByEmployeeId(Long employeeId);

	List<Appointment> findByStatus(AppointmentStatus status);

	List<Appointment> findByDateBetween(LocalDateTime start, LocalDateTime end);

	List<Appointment> findByType(AppointmentType type);

	long countByStatus(AppointmentStatus status);
}
package com.petcare.model.appointment;

import java.util.List;

import com.petcare.enums.AppointmentStatus;
import com.petcare.model.appointment.dto.AppointmentRequest;
import com.petcare.model.appointment.dto.AppointmentResponse;
import com.petcare.model.appointment.dto.AppointmentStatusRequest;

public interface AppointmentService {
	AppointmentResponse createAppointment(AppointmentRequest request);

	List<AppointmentResponse> getAppointmentsByPet(Long petId);

	List<AppointmentResponse> getAppointmentsByEmployee(Long employeeId);

	boolean updateStatus(Long id, AppointmentStatus newStatus);

	boolean updateStatus(Long appointmentId, AppointmentStatus newStatus, Long clientId);

	boolean updateStatus(AppointmentStatusRequest request);

}
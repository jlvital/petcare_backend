package com.petcare.model.appointment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.petcare.enums.AppointmentStatus;
import com.petcare.model.appointment.dto.AppointmentRequest;
import com.petcare.model.appointment.dto.AppointmentStatusRequest;
import com.petcare.model.appointment.dto.AppointmentResponse;
import com.petcare.model.employee.Employee;
import com.petcare.model.employee.EmployeeRepository;
import com.petcare.model.pet.Pet;
import com.petcare.model.pet.PetRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PetRepository petRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public AppointmentResponse createAppointment(AppointmentRequest request) {
        Appointment appointment = new Appointment();
        appointment.setDate(request.getDate());
        appointment.setTime(request.getTime());
        appointment.setType(request.getType());
        appointment.setStatus(AppointmentStatus.PENDIENTE);

        Optional<Pet> petOptional = petRepository.findById(request.getPetId());
        if (!petOptional.isPresent()) {
            log.warn("Mascota no encontrada con ID: {}", request.getPetId());
            throw new IllegalArgumentException("La mascota indicada no existe");
        }

        Optional<Employee> employeeOptional = employeeRepository.findById(request.getEmployeeId());
        if (!employeeOptional.isPresent()) {
            log.warn("Veterinario no encontrado con ID: {}", request.getEmployeeId());
            throw new IllegalArgumentException("El veterinario indicado no existe");
        }

        appointment.setPet(petOptional.get());
        appointment.setEmployee(employeeOptional.get());

        Appointment saved = appointmentRepository.save(appointment);
        log.info("Cita creada correctamente con ID: {}", saved.getId());

        return toResponse(saved);
    }

    @Override
    public List<AppointmentResponse> getAppointmentsByPet(Long petId) {
        List<Appointment> appointments = appointmentRepository.findByPetId(petId);
        List<AppointmentResponse> responses = new ArrayList<>();
        for (Appointment appointment : appointments) {
            responses.add(toResponse(appointment));
        }
        return responses;
    }

    @Override
    public List<AppointmentResponse> getAppointmentsByEmployee(Long employeeId) {
        List<Appointment> appointments = appointmentRepository.findByEmployeeId(employeeId);
        List<AppointmentResponse> responses = new ArrayList<>();
        for (Appointment appointment : appointments) {
            responses.add(toResponse(appointment));
        }
        return responses;
    }

    @Override
    public boolean updateStatus(AppointmentStatusRequest request) {
        return updateStatus(request.getAppointmentId(), request.getNewStatus());
    }

    @Override
    public boolean updateStatus(Long appointmentId, AppointmentStatus newStatus) {
        if (appointmentId == null || appointmentId <= 0) {
            log.warn("ID de cita inválido: {}", appointmentId);
            return false;
        }

        if (newStatus == null) {
            log.warn("Nuevo estado no proporcionado para la cita con ID: {}", appointmentId);
            return false;
        }

        Optional<Appointment> optional = appointmentRepository.findById(appointmentId);
        if (!optional.isPresent()) {
            log.warn("Cita no encontrada con ID: {}", appointmentId);
            return false;
        }

        Appointment appointment = optional.get();
        appointment.setStatus(newStatus);
        appointmentRepository.save(appointment);

        log.info("Estado de cita actualizado a {} para la cita con ID: {}", newStatus, appointmentId);
        return true;
    }

    @Override
    public boolean updateStatus(Long appointmentId, AppointmentStatus newStatus, Long clientId) {
        if (appointmentId == null || appointmentId <= 0) {
            log.warn("ID de cita inválido: {}", appointmentId);
            return false;
        }

        if (newStatus == null) {
            log.warn("Nuevo estado no proporcionado para la cita con ID: {}", appointmentId);
            return false;
        }

        Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
        if (!appointmentOpt.isPresent()) {
            log.warn("Cita no encontrada con ID: {}", appointmentId);
            return false;
        }

        Appointment appointment = appointmentOpt.get();

        if (appointment.getPet() == null || appointment.getPet().getClient() == null) {
            log.warn("La cita con ID {} no tiene mascota o cliente asociado", appointmentId);
            return false;
        }

        Long ownerId = appointment.getPet().getClient().getId();
        if (!ownerId.equals(clientId)) {
            log.warn("El cliente con ID {} intentó modificar una cita que no le pertenece (dueño real: {})", clientId, ownerId);
            return false;
        }

        appointment.setStatus(newStatus);
        appointmentRepository.save(appointment);
        log.info("Cliente con ID {} actualizó el estado de su cita {} a {}", clientId, appointmentId, newStatus);
        return true;
    }

    private AppointmentResponse toResponse(Appointment appt) {
        AppointmentResponse response = AppointmentResponse.builder()
            .id(appt.getId())
            .date(appt.getDate())
            .time(appt.getTime())
            .status(appt.getStatus())
            .type(appt.getType())
            .petName(appt.getPet() != null ? appt.getPet().getName() : "Desconocido")
            .veterinarianName(appt.getEmployee() != null ? appt.getEmployee().getName() : "Sin asignar")
            .build();

        return response;
    }
}

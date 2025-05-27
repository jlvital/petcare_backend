package com.petcare.model.appointment;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.petcare.model.appointment.dto.AppointmentRequest;
import com.petcare.model.appointment.dto.AppointmentStatusRequest;
import com.petcare.model.appointment.dto.AppointmentResponse;
import com.petcare.model.client.Client;
import com.petcare.model.employee.Employee;
import com.petcare.model.pet.Pet;
import com.petcare.model.pet.PetRepository;
import com.petcare.model.user.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
@Slf4j
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final PetRepository petRepository;

    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(@Valid @RequestBody AppointmentRequest request) {
        log.info("Solicitud para crear nueva cita: {}", request);
        AppointmentResponse response = appointmentService.createAppointment(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('CLIENTE') or hasRole('EMPLEADO')")
    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByPet(
            @PathVariable Long petId,
            @AuthenticationPrincipal User User) {

        if (petId == null || petId <= 0) {
            log.warn("ID de mascota inválido: {}", petId);
            return ResponseEntity.badRequest().build();
        }

        if (User == null) {
            log.warn("Usuario no autenticado intentando acceder a citas");
            return ResponseEntity.status(401).build();
        }

        if (User instanceof Client) {
            Optional<Pet> petOptional = petRepository.findById(petId);
            if (!petOptional.isPresent()) {
                log.warn("Mascota no encontrada con ID: {}", petId);
                return ResponseEntity.notFound().build();
            }

            Pet pet = petOptional.get();
            Client owner = pet.getClient();
            if (owner == null || !owner.getId().equals(User.getId())) {
                log.warn("Acceso denegado: Cliente con ID: {} intentó acceder a mascota con ID: {} que no le pertenece", User.getId(), petId);
                return ResponseEntity.status(403).build(); // Forbidden
            }
        }

        log.info("Obteniendo citas para mascota con ID: {} por usuario con ID: {}", petId, User.getId());
        List<AppointmentResponse> citas = appointmentService.getAppointmentsByPet(petId);

        return citas == null || citas.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(citas);
    }

    @PreAuthorize("hasRole('EMPLEADO')")
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByEmployee(
            @PathVariable Long employeeId,
            @AuthenticationPrincipal User User) {

        if (employeeId == null || employeeId <= 0) {
            log.warn("ID de empleado inválido: {}", employeeId);
            return ResponseEntity.badRequest().build();
        }

        if (User == null) {
            log.warn("Usuario no autenticado intentando acceder a citas");
            return ResponseEntity.status(401).build();
        }

        if (User instanceof Employee) {
            if (!User.getId().equals(employeeId)) {
                log.warn("Acceso denegado: Empleado con ID: {} intentó acceder a citas de otro empleado con ID: {}", User.getId(), employeeId);
                return ResponseEntity.status(403).build();
            }
        } else {
            log.warn("Acceso denegado: Usuario con rol no autorizado accediendo a citas de empleado");
            return ResponseEntity.status(403).build();
        }

        log.info("Obteniendo citas para veterinario con ID: {} por el mismo usuario", employeeId);
        List<AppointmentResponse> list = appointmentService.getAppointmentsByEmployee(employeeId);

        return list == null || list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    @PreAuthorize("hasRole('CLIENTE') or hasRole('EMPLEADO')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateAppointmentStatus(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentStatusRequest request,
            @AuthenticationPrincipal User User) {

        if (id == null || id <= 0) {
            log.warn("ID de cita inválido: {}", id);
            return ResponseEntity.badRequest().body("ID de cita inválido.");
        }

        if (request.getNewStatus() == null) {
            log.warn("Estado de cita no proporcionado.");
            return ResponseEntity.badRequest().body("Debe proporcionar un nuevo estado válido.");
        }

        boolean updated;
        if (User instanceof Client) {
            updated = appointmentService.updateStatus(id, request.getNewStatus(), User.getId());
        } else {
            updated = appointmentService.updateStatus(id, request.getNewStatus());
        }

        if (updated) {
            log.info("Estado actualizado correctamente para la cita con ID: {}", id);
            return ResponseEntity.ok("Estado actualizado correctamente.");
        } else {
            log.warn("No se pudo actualizar el estado para la cita con ID: {}", id);
            return ResponseEntity.badRequest().body("No se pudo actualizar el estado.");
        }
    }
}
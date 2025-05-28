package com.petcare.model.booking;

import com.petcare.enums.BookingStatus;
import com.petcare.model.booking.dto.*;
import com.petcare.model.client.Client;
import com.petcare.model.employee.Employee;
import com.petcare.model.user.User;
import com.petcare.utils.RoleChecker;
import com.petcare.validations.ClientValidator;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    // ════════════════════════════════════════════════════════
    // CLIENTE: Crear nueva cita
    // ════════════════════════════════════════════════════════
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<BookingResponse> createBooking(
            @Valid @RequestBody BookingRequest request,
            @AuthenticationPrincipal Client client) {

        ClientValidator.validateAuthenticatedClient(client);
        BookingResponse response = bookingService.createBooking(request, client);

        log.info("📅 Cliente ID {} ha agendado una nueva cita (ID: {}).", client.getId(), response.getId());
        return ResponseEntity.ok(response);
    }

    // ════════════════════════════════════════════════════════
    // CLIENTE: Modificar una cita existente
    // ════════════════════════════════════════════════════════
    @PatchMapping("/{bookingId}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<BookingResponse> updateBooking(
            @PathVariable Long bookingId,
            @Valid @RequestBody BookingUpdateRequest request,
            @AuthenticationPrincipal Client client) {

        ClientValidator.validateAuthenticatedClient(client);
        BookingResponse response = bookingService.updateBooking(bookingId, client.getId(), request);

        log.info("✏️ Cliente ID {} ha modificado la cita ID {}.", client.getId(), bookingId);
        return ResponseEntity.ok(response);
    }

    // ════════════════════════════════════════════════════════
    // CLIENTE: Cancelar cita
    // ════════════════════════════════════════════════════════
    @PutMapping("/client/{bookingId}/cancel")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<String> cancelBooking(
            @PathVariable Long bookingId,
            @AuthenticationPrincipal Client client) {

        boolean updated = bookingService.updateStatus(bookingId, BookingStatus.CANCELADA, client.getId());

        if (updated) {
            log.info("Cliente ID {} ha cancelado la cita ID {}.", client.getId(), bookingId);
            return ResponseEntity.ok("Cita cancelada correctamente.");
        } else {
            log.warn("Fallo al cancelar la cita ID {} por cliente ID {}", bookingId, client.getId());
            return ResponseEntity.badRequest().body("No se pudo cancelar la cita.");
        }
    }

    // ════════════════════════════════════════════════════════
    // EMPLEADO: Obtener sus citas
    // ════════════════════════════════════════════════════════
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasRole('EMPLEADO')")
    public ResponseEntity<List<BookingResponse>> getBookingsByEmployee(
            @PathVariable Long employeeId,
            @AuthenticationPrincipal User user) {

        if (!RoleChecker.isEmployee(user) || !user.getId().equals(employeeId)) {
            log.warn("⛔ Acceso denegado: Usuario ID {} intentó acceder a las citas del empleado ID {}", user.getId(), employeeId);
            return ResponseEntity.status(403).build();
        }

        List<BookingResponse> list = bookingService.getBookingsByEmployee(employeeId);
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

    // ════════════════════════════════════════════════════════
    // EMPLEADO: Anular cita
    // ════════════════════════════════════════════════════════
    @PutMapping("/employee/{bookingId}/abort")
    @PreAuthorize("hasRole('EMPLEADO')")
    public ResponseEntity<String> abortBooking(
            @PathVariable Long bookingId,
            @AuthenticationPrincipal Employee employee) {

        boolean updated = bookingService.updateStatus(bookingId, BookingStatus.ANULADA);

        if (updated) {
            log.info("Empleado ID {} anuló la cita ID {}.", employee.getId(), bookingId);
            return ResponseEntity.ok("Cita anulada correctamente.");
        } else {
            log.warn("No se pudo anular la cita ID {} por parte del empleado ID {}", bookingId, employee.getId());
            return ResponseEntity.badRequest().body("No se pudo anular la cita.");
        }
    }

    // ════════════════════════════════════════════════════════
    // EMPLEADO: Marcar cita como completada
    // ════════════════════════════════════════════════════════
    @PutMapping("/employee/{bookingId}/complete")
    @PreAuthorize("hasRole('EMPLEADO')")
    public ResponseEntity<String> completeBooking(
            @PathVariable Long bookingId,
            @AuthenticationPrincipal Employee employee) {

        boolean updated = bookingService.updateStatus(bookingId, BookingStatus.COMPLETADA);

        if (updated) {
            log.info("Empleado ID {} marcó como COMPLETADA la cita ID {}.", employee.getId(), bookingId);
            return ResponseEntity.ok("Cita marcada como completada.");
        } else {
            log.warn("No se pudo marcar como completada la cita ID {} por parte del empleado ID {}", bookingId, employee.getId());
            return ResponseEntity.badRequest().body("No se pudo completar la cita.");
        }
    }
}
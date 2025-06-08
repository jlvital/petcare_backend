package com.petcare.domain.booking;

import com.petcare.domain.booking.dto.*;
import com.petcare.domain.client.Client;
import com.petcare.domain.employee.Employee;
import com.petcare.domain.user.User;
import com.petcare.enums.BookingStatus;
import com.petcare.utils.dto.MessageResponse;
import com.petcare.validators.ClientValidator;
import com.petcare.validators.EmployeeValidator;
import static com.petcare.utils.constants.MessageConstants.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de citas veterinarias.
 * <p>
 * Permite a los clientes crear, consultar, modificar o cancelar citas,
 * y a los empleados ver sus citas asignadas y marcar su estado.
 *
 * @see BookingService
 */
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

	private final BookingService bookingService;

	// ╔══════════════════════════════════════════════════════════════╗
	// ║ CLIENTE - GESTIÓN DE CITAS (CREAR, EDITAR, CONSULTAR)        ║
	// ╚══════════════════════════════════════════════════════════════╝

	/**
	 * Crea una nueva cita para el cliente autenticado.
	 *
	 * @param request Datos de la cita.
	 * @param client Cliente autenticado.
	 * @return Cita registrada con su ID asignado.
	 */
	@PostMapping
	@PreAuthorize("hasRole('CLIENTE')")
	public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request,
			@AuthenticationPrincipal Client client) {
		ClientValidator.validateAuthenticatedClient(client);
		BookingResponse response = bookingService.createBooking(request, client);

		log.info("Cliente ID {} ha agendado una nueva cita (ID: {}).", client.getId(), response.getId());
		return ResponseEntity.ok(response);
	}

	/**
	 * Actualiza parcialmente una cita del cliente.
	 *
	 * @param bookingId ID de la cita a modificar.
	 * @param request Nuevos datos.
	 * @param client Cliente autenticado.
	 * @return Cita actualizada.
	 */
	@PatchMapping("/{bookingId}")
	@PreAuthorize("hasRole('CLIENTE')")
	public ResponseEntity<BookingResponse> updateBooking(@PathVariable Long bookingId,
			@Valid @RequestBody BookingUpdate request, @AuthenticationPrincipal Client client) {
		ClientValidator.validateAuthenticatedClient(client);
		BookingResponse response = bookingService.updateBooking(bookingId, client.getId(), request);

		log.info("Cliente ID {} ha modificado la cita ID {}.", client.getId(), bookingId);
		return ResponseEntity.ok(response);
	}

	/**
	 * Cancela una cita activa por parte del cliente.
	 *
	 * @param bookingId ID de la cita.
	 * @param client Cliente autenticado.
	 * @return Mensaje confirmando la cancelación.
	 */
	@PutMapping("/client/{bookingId}/cancel")
	@PreAuthorize("hasRole('CLIENTE')")
	public ResponseEntity<MessageResponse> cancelBooking(@PathVariable Long bookingId,
	                                                     @AuthenticationPrincipal Client client) {
		bookingService.updateStatus(bookingId, BookingStatus.CANCELADA, client.getId());
		log.info("Cliente ID {} ha cancelado la cita ID {}.", client.getId(), bookingId);
		return ResponseEntity.ok(new MessageResponse(CANCELLED));
	}

	/**
	 * Devuelve las próximas citas del cliente.
	 *
	 * @param client Cliente autenticado.
	 * @return Lista de citas futuras o vacía si no hay ninguna.
	 */
	@GetMapping("/client/upcoming")
	@PreAuthorize("hasRole('CLIENTE')")
	public ResponseEntity<List<BookingResponse>> getUpcomingBookings(@AuthenticationPrincipal Client client) {
		ClientValidator.validateAuthenticatedClient(client);
		List<BookingResponse> list = bookingService.getUpcomingBookingsByClient(client.getId());

		if (list.isEmpty()) {
			log.info("Cliente ID {} no tiene próximas citas.", client.getId());
			return ResponseEntity.noContent().build();
		}

		log.info("Cliente ID {} consultó sus próximas {} citas.", client.getId(), list.size());
		return ResponseEntity.ok(list);
	}

	/**
	 * Devuelve el historial de citas pasadas del cliente.
	 *
	 * @param client Cliente autenticado.
	 * @return Lista de citas anteriores.
	 */
	@GetMapping("/client/history")
	@PreAuthorize("hasRole('CLIENTE')")
	public ResponseEntity<List<BookingResponse>> getPastBookingsByClient(@AuthenticationPrincipal Client client) {
		ClientValidator.validateAuthenticatedClient(client);
		List<BookingResponse> list = bookingService.getPastBookingsByClient(client.getId());

		if (list.isEmpty()) {
			log.info("Cliente ID {} no tiene historial de citas.", client.getId());
			return ResponseEntity.noContent().build();
		}

		log.info("Cliente ID {} consultó su historial con {} citas.", client.getId(), list.size());
		return ResponseEntity.ok(list);
	}

	// ╔══════════════════════════════════════════════════════════════╗
	// ║ EMPLEADO - GESTIÓN DE CITAS (EDITAR, CONSULTAR)        	  ║
	// ╚══════════════════════════════════════════════════════════════╝

	/**
	 * Devuelve todas las citas asociadas a un empleado.
	 *
	 * @param employeeId ID del empleado.
	 * @param user Usuario autenticado.
	 * @return Lista de citas o acceso denegado.
	 */
	@GetMapping("/employee/{employeeId}")
	@PreAuthorize("hasRole('EMPLEADO')")
	public ResponseEntity<List<BookingResponse>> getBookingsByEmployee(@PathVariable Long employeeId,
			@AuthenticationPrincipal User user) {
		EmployeeValidator.validateEmployeeAccess(user, employeeId);

		List<BookingResponse> list = bookingService.getBookingsByEmployee(employeeId);
		return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
	}

	/**
	 * Anula una cita por parte del empleado.
	 *
	 * @param bookingId ID de la cita.
	 * @param employee Empleado autenticado.
	 * @return Mensaje indicando el resultado.
	 */
	@PutMapping("/employee/{bookingId}/abort")
	@PreAuthorize("hasRole('EMPLEADO')")
	public ResponseEntity<MessageResponse> abortBooking(@PathVariable Long bookingId,
	                                                    @AuthenticationPrincipal Employee employee) {
		bookingService.updateStatus(bookingId, BookingStatus.ANULADA);
		log.info("Empleado ID {} anuló la cita ID {}.", employee.getId(), bookingId);
		return ResponseEntity.ok(new MessageResponse(ABORTED));
	}

	/**
	 * Marca una cita como completada por parte del empleado.
	 *
	 * @param bookingId ID de la cita.
	 * @param employee Empleado autenticado.
	 * @return Mensaje confirmando la finalización.
	 */
	@PutMapping("/employee/{bookingId}/complete")
	@PreAuthorize("hasRole('EMPLEADO')")
	public ResponseEntity<MessageResponse> completeBooking(@PathVariable Long bookingId,
	                                                       @AuthenticationPrincipal Employee employee) {
		bookingService.updateStatus(bookingId, BookingStatus.COMPLETADA);
		log.info("Empleado ID {} completó la cita ID {}.", employee.getId(), bookingId);
		return ResponseEntity.ok(new MessageResponse(COMPLETED));
	}

	/**
	 * Devuelve las próximas citas asignadas a un empleado.
	 *
	 * @param employeeId ID del empleado.
	 * @param employee Empleado autenticado.
	 * @return Lista de citas futuras o acceso denegado.
	 */
	@GetMapping("/employee/{employeeId}/upcoming")
	@PreAuthorize("hasRole('EMPLEADO')")
	public ResponseEntity<List<BookingResponse>> getUpcomingBookingsByEmployee(@PathVariable Long employeeId,
			@AuthenticationPrincipal Employee employee) {
		EmployeeValidator.validateEmployeeAccess(employee, employeeId);

		List<BookingResponse> list = bookingService.getUpcomingBookingsByEmployee(employeeId);

		if (list.isEmpty()) {
			log.info("Empleado ID {} no tiene próximas citas asignadas.", employeeId);
			return ResponseEntity.noContent().build();
		}

		log.info("Empleado ID {} consultó sus próximas {} citas.", employeeId, list.size());
		return ResponseEntity.ok(list);
	}
}
package com.petcare.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.petcare.admin.dto.AdminBookingStats;
import com.petcare.admin.dto.AdminServiceStats;
import com.petcare.domain.booking.Booking;
import com.petcare.domain.booking.dto.BookingMapper;
import com.petcare.domain.booking.dto.BookingResponse;
import com.petcare.domain.employee.Employee;
import com.petcare.domain.employee.dto.EmployeeMapper;
import com.petcare.domain.employee.dto.EmployeeRequest;
import com.petcare.domain.employee.dto.EmployeeResponse;
import com.petcare.domain.product.dto.PriceUpdate;
import com.petcare.domain.product.dto.StockUpdate;
import com.petcare.domain.user.User;
import com.petcare.domain.user.UserAccountService;
import com.petcare.domain.user.dto.UserMapper;
import com.petcare.domain.user.dto.UserResponse;
import com.petcare.domain.user.dto.UserUpdate;
import com.petcare.utils.dto.MessageResponse;
import static com.petcare.utils.constants.MessageConstants.*;

import java.util.ArrayList;
import java.util.List;

import static com.petcare.utils.constants.AdminConstants.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST exclusivo para usuarios con rol ADMIN.
 * <p>
 * Permite gestionar usuarios (clientes y empleados), productos, citas y
 * consultar estadísticas generales o por servicios.
 * <p>
 * Todos los endpoints de esta clase requieren privilegios de administrador.
 *
 * @see AdminService
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

	private final AdminService adminService;
	private final UserAccountService userAccountService;


	// ╔════════════════════════════════════════════════════╗
	// ║ PANEL DE ADMINISTRADOR ║
	// ╚════════════════════════════════════════════════════╝

	/**
	 * Muestra un mensaje de bienvenida al panel del administrador.
	 *
	 * @return Texto plano de saludo para confirmar el acceso.
	 */
	@GetMapping("/dashboard")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<MessageResponse> getDashboard() {
		log.info("Acceso concedido al panel del administrador.");
		return ResponseEntity.ok(new MessageResponse(ADMIN + ADMIN_NAME));
	}

	// ╔════════════════════════════════════════════════════╗
	// ║ GESTIÓN DE USUARIOS ║
	// ╚════════════════════════════════════════════════════╝

	/**
	 * Permite al administrador consultar los datos públicos de cualquier usuario
	 * (cliente o empleado) a partir de su ID.
	 *
	 * @param userId ID del usuario a consultar.
	 * @return Datos públicos del usuario en formato DTO.
	 */
	@GetMapping("/user/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
		log.info("Solicitada información del usuario con ID: {}", userId);
		User user = adminService.findUserById(userId);
		UserResponse response = UserMapper.toResponse(user);
		return ResponseEntity.ok(response);
	}

	/**
	 * Permite al administrador modificar los datos personales de cualquier usuario.
	 * Solo se actualizarán los campos que se incluyan en el cuerpo de la petición.
	 *
	 * @param userId  ID del usuario a modificar.
	 * @param request Datos nuevos que se desean aplicar.
	 * @return Respuesta vacía si la actualización ha sido correcta.
	 */
	@PutMapping("/update-user/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> updateUser(@PathVariable Long userId, @RequestBody @Valid UserUpdate request) {
		log.info("Solicitud de actualización para el usuario con ID: {}", userId);
		adminService.updateUser(userId, request);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/users/definitive/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<MessageResponse> definitiveDeleteUser(@PathVariable Long id) {
		userAccountService.deleteUserPermanently(id);
		return ResponseEntity.ok(new MessageResponse(USER_DELETED));
	}

	@GetMapping("/users")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		log.info("El administrador ha solicitado la lista de usuarios.");

		List<User> users = adminService.getAllUsers();
		List<UserResponse> response = new ArrayList<>();

		for (User user : users) {
			response.add(UserMapper.toResponse(user));
		}

		return ResponseEntity.ok(response);
	}
	
	// ╔════════════════════════════════════════════════════╗
	// ║ GESTIÓN DE EMPLEADOS ║
	// ╚════════════════════════════════════════════════════╝

	/**
	 * Permite al administrador registrar un nuevo empleado en el sistema.
	 *
	 * @param request Datos del empleado que se desea registrar.
	 * @return Datos del empleado registrado con ID asignado.
	 */
	@PostMapping("/register-employee")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<EmployeeResponse> registerEmployee(@RequestBody @Valid EmployeeRequest request) {
		log.info("Registro solicitado para nuevo empleado: {} {} <{}>", request.getName(), request.getLastName1(),
				request.getRecoveryEmail());

		Employee empleado = adminService.registerEmployee(request);
		EmployeeResponse response = EmployeeMapper.toResponse(empleado);

		log.info("Empleado registrado correctamente. ID asignado: {}", empleado.getId());
		return ResponseEntity.ok(response);
	}

	// ╔════════════════════════════════════════════════════╗
	// ║ GESTIÓN DE PRODUCTOS ║
	// ╚════════════════════════════════════════════════════╝

	/**
	 * Permite al administrador actualizar el precio de un producto.
	 *
	 * @param request Objeto con el ID del producto y su nuevo precio.
	 * @return Respuesta vacía si la actualización se realizó con éxito.
	 */
	@PutMapping("/update-price")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> updateProductPrice(@RequestBody @Valid PriceUpdate request) {
		log.info("Solicitud de actualización de precio: producto ID {} nuevo precio {}", request.getProductId(),
				request.getNewPrice());
		adminService.updateProductPrice(request.getProductId(), request.getNewPrice());
		return ResponseEntity.noContent().build();
	}

	/**
	 * Permite al administrador modificar la cantidad disponible (stock) de un
	 * producto.
	 *
	 * @param request Objeto con el ID del producto y la nueva cantidad.
	 * @return Respuesta vacía si la operación se completó correctamente.
	 */
	@PutMapping("/update-stock")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> updateStock(@RequestBody @Valid StockUpdate request) {
		log.info("Solicitud de actualización de stock: producto ID {} nueva cantidad {}", request.getProductId(),
				request.getQuantity());
		adminService.updateStock(request.getProductId(), request.getQuantity());
		return ResponseEntity.noContent().build();
	}

	// ╔════════════════════════════════════════════════════╗
	// ║ ESTADÍSTICAS Y CITAS ║
	// ╚════════════════════════════════════════════════════╝

	/**
	 * Devuelve un resumen estadístico del sistema: número de citas, clientes,
	 * empleados, etc.
	 *
	 * @return Objeto con estadísticas globales del sistema.
	 */
	@GetMapping("/stats")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<AdminBookingStats> getStats() {
		log.info("Solicitando estadísticas del panel de administración...");
		AdminBookingStats stats = adminService.getBookingStats();
		return ResponseEntity.ok(stats);
	}

	/**
	 * Devuelve estadísticas agrupadas por tipo de servicio (baño, revisión,
	 * peluquería...).
	 *
	 * @return Objeto con porcentajes y servicios más y menos demandados.
	 */
	@GetMapping("/service-stats")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<AdminServiceStats> getServiceStats() {
		log.info("Solicitando estadísticas por servicio...");
		AdminServiceStats stats = adminService.getServiceStats();
		return ResponseEntity.ok(stats);
	}

	/**
	 * Devuelve una lista paginada de citas recientes, con opción de filtrado por
	 * días.
	 *
	 * @param days Número de días atrás desde hoy (opcional).
	 * @param page Número de página actual.
	 * @param size Tamaño de la página.
	 * @param sort Parámetro de ordenación (ej: date,asc).
	 * @return Página de resultados con las citas filtradas y ordenadas.
	 */
	@GetMapping("/bookings")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Page<BookingResponse>> getAllBookings(
			@RequestParam(value = "days", required = false) Integer days,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(value = "sort", defaultValue = "date,asc") String sort) {
		log.info("Administrador solicitó citas con filtro: días={}, página={}, tamaño={}, orden={}", days, page, size,
				sort);

		String[] sortParams = sort.split(",");
		Pageable pageable = PageRequest.of(page, size,
				Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]));

		Page<Booking> pageResult = adminService.getBookingsFromLastDays(days, pageable);

		if (pageResult.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		Page<BookingResponse> responsePage = pageResult.map(BookingMapper::toResponse);
		return ResponseEntity.ok(responsePage);
	}
}
package com.petcare.domain.employee;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petcare.domain.employee.dto.EmployeeMapper;
import com.petcare.domain.employee.dto.EmployeeResponse;
import com.petcare.domain.employee.dto.EmployeeUpdate;
import com.petcare.utils.dto.MessageResponse;
import com.petcare.validators.EmployeeValidator;
import static com.petcare.utils.constants.MessageConstants.*;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST exclusivo para empleados.
 * <p>
 * Permite acceder al panel del empleado y modificar los datos de su perfil.
 * <p>
 * Todos los endpoints requieren autenticación con rol EMPLEADO.
 *
 * @see EmployeeService
 * @see com.petcare.domain.employee.dto.EmployeeUpdate
 */
@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

	private final EmployeeService employeeService;

	// ╔══════════════════════════════════════════════════════════════╗
	// ║ PANEL DEL EMPLEADO ║
	// ╚══════════════════════════════════════════════════════════════╝

	/**
	 * Devuelve un mensaje de bienvenida al panel del empleado autenticado.
	 *
	 * @return Saludo de bienvenida en texto plano.
	 */
	@GetMapping("/dashboard")
	@PreAuthorize("hasRole('EMPLEADO')")
	public ResponseEntity<MessageResponse> getClientDashboard(@AuthenticationPrincipal Employee employee) {
		EmployeeValidator.validateAuthenticatedEmployee(employee);
		log.info("Acceso concedido al panel del cliente ID {}", employee.getId());
		return ResponseEntity.ok(new MessageResponse(CLIENT + employee.getName()));
	}

	/**
	 * Permite al empleado actualizar los datos de su perfil.
	 *
	 * @param request  Datos nuevos que se desean aplicar en el perfil.
	 * @param employee Empleado autenticado que solicita la modificación.
	 * @return Mensaje de confirmación si la operación fue exitosa.
	 */
	@PutMapping("/profile")
	@PreAuthorize("hasRole('EMPLEADO')")
	public ResponseEntity<MessageResponse> updateEmployeeProfile(@RequestBody EmployeeUpdate request,
	                                                             @AuthenticationPrincipal Employee employee) {
		EmployeeValidator.validateAuthenticatedEmployee(employee);
		employeeService.updateEmployeeProfile(employee, request);
		log.info("Empleado ID {} actualizó su perfil correctamente", employee.getId());
		return ResponseEntity.ok(new MessageResponse(PROFILE_UPDATED));
	}
	
	@GetMapping("/profile")
	@PreAuthorize("hasRole('EMPLEADO')")
	public ResponseEntity<EmployeeResponse> getEmployeeProfile(@AuthenticationPrincipal Employee employee) {
	    EmployeeValidator.validateAuthenticatedEmployee(employee);
	    log.info("Empleado ID {} accedió a su perfil personal", employee.getId());
	    return ResponseEntity.ok(EmployeeMapper.toResponse(employee));
	}
}
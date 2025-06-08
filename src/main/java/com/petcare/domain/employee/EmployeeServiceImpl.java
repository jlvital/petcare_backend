package com.petcare.domain.employee;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.petcare.auth.security.JwtUtil;
import com.petcare.domain.employee.dto.EmployeeMapper;
import com.petcare.domain.employee.dto.EmployeeRequest;
import com.petcare.domain.employee.dto.EmployeeUpdate;
import com.petcare.domain.user.UserRepository;
import com.petcare.enums.Role;
import com.petcare.notification.SystemEmailService;
import com.petcare.utils.constants.*;
import com.petcare.validators.EmployeeValidator;
import com.petcare.validators.UserValidator;
import com.petcare.utils.PasswordUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final SystemEmailService systemEmailService;
	private final JwtUtil jwtUtil;

	/**
	 * Busca un empleado por su ID. Si no se encuentra o no es un empleado válido, lanza excepción.
	 */
	
	@Override
	public Employee findEmployeeById(Long id) {
	    return EmployeeValidator.requireEmployeeById(userRepository, id);
	}

	/**
	 * Busca un empleado por su username corporativo.
	 */
	
	@Override
	public Employee findEmployeeByUsername(String username) {
	    return EmployeeValidator.requireEmployeeByUsername(userRepository, username);
	}

	/**
	 * Registra un nuevo empleado a partir de los datos de formulario de administración.
	 * Genera usuario corporativo, contraseña temporal y envía correo de bienvenida.
	 */

	@Override
	public Employee registerEmployee(EmployeeRequest request) {
	    // Transformar DTO a entidad
	    Employee newEmployee = EmployeeMapper.toEntity(request);

	    String uniqueUsername = EmployeeValidator.generateUniqueCorporateEmail(
	        userRepository,
	        newEmployee.getName(),
	        newEmployee.getLastName1(),
	        newEmployee.getLastName2()
	    );
	    newEmployee.setUsername(uniqueUsername);
	    newEmployee.setRole(Role.EMPLEADO);

	    String password = PasswordUtil.generateRandomPassword(8);
	    newEmployee.setPassword(passwordEncoder.encode(password));

	    UserValidator.validateUsernameAndRecoveryEmail(newEmployee);
	    EmployeeValidator.checkRecoveryEmail(userRepository, newEmployee, newEmployee.getRecoveryEmail());

	    Employee saved = userRepository.save(newEmployee);
	    log.info("Empleado registrado correctamente. ID: {}", saved.getId());

	    String token = jwtUtil.generateToken(saved.getUsername(), saved.getRole().name());
	    String resetLink = UrlConstants.buildRecoveryLink(token);
	    
	    saved.setRecoveryToken(token);
	    saved.setRecoveryTokenExpiration(LocalDateTime.now().plusHours(1));
	    userRepository.save(saved);
	    
	    systemEmailService.sendWelcomeEmail(
	    	    saved.getRecoveryEmail(),
	    	    Role.EMPLEADO.getLabel(),
	    	    saved.getName(),
	    	    saved.getUsername(),
	    	    password,
	    	    resetLink
	    	);

	    return saved;
	}

	/**
	 * Actualiza parcialmente el perfil del empleado autenticado.
	 * Solo se modificarán los campos presentes en el DTO de entrada.
	 * Se valida que el nuevo correo de recuperación no esté duplicado.
	 *
	 * @param employee Empleado autenticado que desea actualizar sus datos
	 * @param request  Datos nuevos del perfil (opcionales)
	 * @throws IllegalArgumentException si los datos son nulos o inválidos
	 */
	
	@Override
	public void updateEmployeeProfile(Employee employee, EmployeeUpdate request) {
		if (employee == null || employee.getId() == null) {
			log.warn("Empleado no válido para actualización.");
			throw new IllegalArgumentException("Empleado inválido para actualizar perfil.");
		}

		if (request == null) {
			log.warn("Datos de actualización nulos para empleado ID {}", employee.getId());
			throw new IllegalArgumentException("Los datos de actualización no pueden ser nulos.");
		}

		// Validar que el nuevo correo no esté ya en uso
		EmployeeValidator.checkRecoveryEmail(userRepository, employee, request.getRecoveryEmail());

		// Actualizar usando mapper
		EmployeeMapper.updateEntityFromRequest(request, employee);

		userRepository.save(employee);
		log.info("Perfil del empleado con ID {} actualizado correctamente.", employee.getId());
	}
}
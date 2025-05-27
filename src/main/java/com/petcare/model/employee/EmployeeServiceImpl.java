package com.petcare.model.employee;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.petcare.auth.security.JwtUtil;
import com.petcare.email.EmailService;
import com.petcare.enums.Role;
import com.petcare.exceptions.UserNotFoundException;
import com.petcare.model.employee.dto.EmployeeUpdateRequest;
import com.petcare.model.user.User;
import com.petcare.model.user.UserRepository;
import com.petcare.utils.Constants;
import com.petcare.utils.PasswordUtil;
import com.petcare.validations.EmployeeValidator;
import com.petcare.validations.UserValidator;

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
	private final EmailService emailService;
	private final JwtUtil jwtUtil;

	@Override
	public Employee findEmployeeById(Long id) {
		Optional<User> optional = userRepository.findById(id);

		if (!optional.isPresent() || !(optional.get() instanceof Employee)) {
			log.warn("Empleado no encontrado con ID: {}", id);
			throw new UserNotFoundException("Empleado no encontrado con ID: " + id);
		}

		return (Employee) optional.get();
	}

	@Override
	public Employee findEmployeeByUsername(String username) {
		Optional<User> optional = userRepository.findByUsername(username);

		if (!optional.isPresent() || !(optional.get() instanceof Employee)) {
			log.warn("Empleado no encontrado con username: {}", username);
			throw new UserNotFoundException("Empleado no encontrado con username: " + username);
		}

		return (Employee) optional.get();
	}

	@Override
	public Employee registerEmployee(Employee newEmployee) {
		// Generar correo corporativo si no viene ya asignado
		String corporateUsername = generateCorporateUsername(
			newEmployee.getName(),
			newEmployee.getLastName1(),
			newEmployee.getLastName2()
		);

		newEmployee.setUsername(corporateUsername + Constants.MAIL_DOMAIN);
		newEmployee.setRole(Role.EMPLEADO);

		// Generar y codificar la contraseña
		String password = PasswordUtil.generateTemporaryPassword(8);
		String encodedPassword = passwordEncoder.encode(password);
		newEmployee.setPassword(encodedPassword);

		// Validaciones
		UserValidator.validateUsernameAndRecoveryEmail(newEmployee);
		EmployeeValidator.validateRecoveryEmailUniqueness(userRepository, newEmployee, newEmployee.getRecoveryEmail());

		// Persistencia
		Employee saved = userRepository.save(newEmployee);
		log.info("Empleado registrado con ID: {}", saved.getId());

		// Generación de token y enlace
		String token = jwtUtil.generateToken(saved.getUsername(), saved.getRole().name());
		String resetLink = Constants.buildRecoveryLink(token);

		// Envío de email de bienvenida
		emailService.sendWelcomeEmail(
			saved.getRecoveryEmail(),
			"EMPLEADO",
			saved.getName(),
			saved.getUsername(),
			password,
			resetLink
		);

		return saved;
	}

	@Override
	@Transactional
	public void updateEmployeeProfile(Employee employee, EmployeeUpdateRequest request) {
		if (employee == null || employee.getId() == null) {
			log.warn("Empleado no válido para actualización.");
			throw new IllegalArgumentException("Empleado inválido para actualizar perfil.");
		}

		if (request == null) {
			log.warn("Datos de actualización nulos para empleado ID {}", employee.getId());
			throw new IllegalArgumentException("Los datos de actualización no pueden ser nulos.");
		}

		employee.setName(request.getName());
		employee.setLastName1(request.getLastName1());
		employee.setLastName2(request.getLastName2());

		EmployeeValidator.validateRecoveryEmailUniqueness(userRepository, employee, request.getRecoveryEmail());
		employee.setRecoveryEmail(request.getRecoveryEmail());

		employee.setPhoneNumber(request.getPhoneNumber());
		employee.setAddress(request.getAddress());
		employee.setGender(request.getGender());

		userRepository.save(employee);
		log.info("Perfil del empleado con ID {} actualizado correctamente.", employee.getId());
	}

	// ✅ Lógica centralizada para construir el username
	private String generateCorporateUsername(String name, String lastName1, String lastName2) {
		String n = name != null ? name.trim().toLowerCase().replace(" ", "") : "";
		String l1 = lastName1 != null ? lastName1.trim().toLowerCase().replace(" ", "") : "";
		String l2 = lastName2 != null ? lastName2.trim().toLowerCase().replace(" ", "") : "";
		return n + "." + l1 + "." + l2;
	}
}

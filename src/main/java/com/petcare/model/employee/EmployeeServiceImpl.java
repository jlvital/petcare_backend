package com.petcare.model.employee;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.petcare.email.EmailService;
import com.petcare.enums.Role;
import com.petcare.exceptions.UserNotFoundException;
import com.petcare.model.user.User;
import com.petcare.model.user.UserRepository;
import com.petcare.utils.Constants;
import com.petcare.utils.PasswordUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

	private final UserRepository userRepository; // Sustituye a EmployeeRepository
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;

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
		String corporateUsername = generateCorporateUsername(newEmployee.getName(), newEmployee.getLastName1(),
				newEmployee.getLastName2());
		newEmployee.setUsername(corporateUsername + Constants.MAIL_DOMAIN);
		newEmployee.setRole(Role.EMPLEADO);

		// Generar y codificar la contraseña
		String password = PasswordUtil.generateTemporaryPassword(8);
		String encodedPassword = passwordEncoder.encode(password);
		newEmployee.setPassword(encodedPassword);

		// Guardar en base de datos
		Employee saved = userRepository.save(newEmployee); // sigue funcionando correctamente
		log.info("Empleado registrado con ID: {}", saved.getId());

		emailService.sendWelcomeEmail(saved.getRecoveryEmail(), 
				"EMPLEADO", 
				saved.getName(), 
				saved.getUsername(),
				password);
		return saved;
	}

	// ✅ Lógica centralizada para construir el username
	private String generateCorporateUsername(String name, String lastName1, String lastName2) {
		String n = name != null ? name.trim().toLowerCase().replace(" ", "") : "";
		String l1 = lastName1 != null ? lastName1.trim().toLowerCase().replace(" ", "") : "";
		String l2 = lastName2 != null ? lastName2.trim().toLowerCase().replace(" ", "") : "";
		return n + "." + l1 + "." + l2;
	}
}

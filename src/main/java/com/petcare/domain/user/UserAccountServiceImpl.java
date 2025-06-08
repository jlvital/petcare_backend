package com.petcare.domain.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.petcare.auth.security.JwtUtil;
import com.petcare.domain.client.Client;
import com.petcare.domain.client.ClientRepository;
import com.petcare.domain.employee.Employee;
import com.petcare.domain.employee.EmployeeRepository;
import com.petcare.enums.AccountStatus;
import com.petcare.enums.Role;
import com.petcare.exceptions.BusinessException;
import com.petcare.utils.constants.SecurityConstants;
import com.petcare.validators.UserValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAccountServiceImpl implements UserAccountService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final ClientRepository clientRepository;
	private final EmployeeRepository employeeRepository;

	private final UserService userService;

	@Override
	public void activateAccount(Long id) {
		User user = userService.getUserById(id);
		user.setAccountStatus(AccountStatus.ACTIVA);
		userService.saveForUserType(user);
		log.info("Cuenta ACTIVA para el usuario con ID: {}", id);
	}

	@Override
	public void deactivateAccount(Long id) {
		User user = userService.getUserById(id);
		user.setAccountStatus(AccountStatus.DESACTIVADA);
		userService.saveForUserType(user);
		log.info("Cuenta desactivada para el usuario con ID: {}", id);
	}

	@Override
	public void deleteUserAccount(Long id) {
	    
		User user = UserValidator.requireUserById(userRepository, id);

	    if (user.getRole() == Role.ADMIN) {
		    log.warn("Intento de borrar al administrador del sistema. Operación denegada.");
		    throw new BusinessException("No se puede eliminar la cuenta de administrador.");
		}
	    
		user.setAccountStatus(AccountStatus.ELIMINADA);
		user.setPassword(SecurityConstants.DEFAULT_PASSWORD);
	    user.setRecoveryToken(null);
	    user.setRecoveryTokenExpiration(null);
	    
	    userService.saveForUserType(user);
	    
		log.info("Cuenta eliminada para el usuario con username: {}", user.getUsername());
	}
	
	@Override
	public void deleteUserPermanently(Long id) {
	    User user = UserValidator.requireUserById(userRepository, id);

	    if (user.getRole() == Role.ADMIN) {
	        log.warn("Intento de eliminación definitiva del administrador. Acción no permitida.");
	        throw new BusinessException("No se puede eliminar al administrador del sistema.");
	    }

	    if (user instanceof Client && user.getRole() == Role.CLIENTE) {
	        clientRepository.delete((Client) user);
	        log.info("Cliente con ID {} eliminado permanentemente del sistema.", id);
	        return;
	    }

	    if (user instanceof Employee && user.getRole() == Role.EMPLEADO) {
	        employeeRepository.delete((Employee) user);
	        log.info("Empleado con ID {} eliminado permanentemente del sistema.", id);
	        return;
	    }

	    log.warn("Tipo de usuario no válido para eliminación definitiva. ID: {}", id);
	    throw new BusinessException("El tipo de usuario no es válido para ser eliminado.");
	}

	@Override
	public List<User> findInactiveUsers(int days) {
		LocalDateTime cutoff = LocalDateTime.now().minusDays(days);
		return userRepository.findByLastAccessBeforeAndAccountStatus(cutoff, AccountStatus.ACTIVA);
	}

	@Override
	public Optional<User> findByRecoveryToken(String token) {
		if (token == null || token.isEmpty()) {
			return Optional.empty();
		}

		return userRepository.findByRecoveryToken(token);
	}

	@Override
	public Optional<User> findByEmailForRecovery(String email) {
		if (email == null || email.isEmpty()) {
			return Optional.empty();
		}

		return userRepository.findByRecoveryEmail(email);
	}

	@Override
	public boolean updatePasswordWithToken(String token, String newPassword) {
		String email = jwtUtil.extractUsername(token);
		User user = userService.getUserByUsername(email);

		if (user.getRecoveryToken() == null || !user.getRecoveryToken().equals(token)) {
			log.warn("Token de recuperación inválido para el usuario: {}", email);
			return false;
		}

		if (user.getRecoveryTokenExpiration() == null
				|| user.getRecoveryTokenExpiration().isBefore(LocalDateTime.now())) {
			log.warn("Token expirado para el usuario: {}", email);
			return false;
		}

		user.setPassword(passwordEncoder.encode(newPassword));
		user.setLastPasswordChange(LocalDateTime.now());
		user.setFailedLoginAttempts(0);

		if (user.getAccountStatus() == AccountStatus.BLOQUEADA) {
			user.setAccountStatus(AccountStatus.ACTIVA);
			log.info("Cuenta desbloqueada para el usuario: {}", email);
		}

		user.setRecoveryToken(null);
		user.setRecoveryTokenExpiration(null);
		userService.saveForUserType(user);

		log.info("Contraseña actualizada correctamente para el usuario: {}", email);
		return true;
	}
}
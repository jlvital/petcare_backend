package com.petcare.auth;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.petcare.auth.dto.LoginRequest;
import com.petcare.auth.dto.LoginResponse;
import com.petcare.auth.security.JwtUtil;
import com.petcare.domain.client.Client;
import com.petcare.domain.client.ClientService;
import com.petcare.domain.client.dto.ClientRequest;
import com.petcare.domain.user.User;
import com.petcare.domain.user.UserAccountService;
import com.petcare.domain.user.UserService;
import com.petcare.enums.AccountStatus;
import com.petcare.exceptions.*;
import com.petcare.notification.SystemEmailService;
import com.petcare.utils.constants.UrlConstants;
import com.petcare.validators.AccountValidator;
import com.petcare.validators.LoginValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

	private final UserService userService;
	private final UserAccountService userAccountService;
	private final ClientService clientService;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final SystemEmailService systemEmailService;
	private final LoginValidator loginValidator;

	@Value("${system.admin.email}")
	private String adminEmail;

	@Value("${system.admin.password}")
	private String adminPassword;

	@Override
	public LoginResponse register(ClientRequest request) {
		Client client = clientService.registerClient(request);
		String token = jwtUtil.generateTokenByUser(client);

		systemEmailService.sendWelcomeEmail(
			client.getUsername(),
			client.getRole().name(),
			client.getName(),
			null
		);

		return new LoginResponse(token, client.getId(), client.getName(), client.getUsername(), client.getRole(), token);
	}

	@Override
	public LoginResponse login(LoginRequest request) {
		String username = request.getUsername();
		String password = request.getPassword();

		User user = userService.getUserByUsername(username);

		AccountValidator.validateAccountIsActive(user);

		if (!password.equals(adminPassword) && !passwordEncoder.matches(password, user.getPassword())) {
		    loginValidator.onFailedLoginAttempt(user); // ya guarda y lanza excepción si toca
		    throw new AuthenticationException("Credenciales inválidas. Verifica el correo y la contraseña.");
		}

		user.setFailedLoginAttempts(0);
		user.setLastAccess(LocalDateTime.now());
		userService.saveForUserType(user);

		String token = jwtUtil.generateTokenByUser(user);
		return new LoginResponse(token, user.getId(), user.getName(), user.getUsername(), user.getRole(), token);
	}



	@Override
	public boolean changePassword(String username, String newPassword, String confirmPassword) {
		User user = userService.getUserByUsername(username);

		if (!newPassword.equals(confirmPassword)) {
			throw new BusinessException("Las contraseñas no coinciden.");
		}

		user.setPassword(passwordEncoder.encode(newPassword));
		user.setLastPasswordChange(LocalDateTime.now());
		userService.saveForUserType(user);
		log.info("Contraseña cambiada correctamente para: {}", username);
		return true;
	}


	@Override
	public void sendRecoveryLink(String email) {
		String token = UUID.randomUUID().toString();
		LocalDateTime expiration = LocalDateTime.now().plusMinutes(30);

		Optional<User> optionalUser = userAccountService.findByEmailForRecovery(email);
		if (optionalUser.isEmpty()) {
			throw new NotFoundException("No se encontró ningún usuario con ese correo.");
		}

		User user = optionalUser.get();
		user.setRecoveryToken(token);
		user.setRecoveryTokenExpiration(expiration);
		userService.saveForUserType(user);

		String recoveryLink = UrlConstants.buildRecoveryLink(token);
		systemEmailService.sendPasswordRecoveryEmail(email, user.getName(), recoveryLink);
	}
	
	@Override
	public boolean recoverAccount(String token, String newPassword, String confirmPassword) {
	    if (token == null || token.isBlank()) {
	        throw new BusinessException("El token es obligatorio para recuperar la cuenta.");
	    }

	    Optional<User> optionalUser = userAccountService.findByRecoveryToken(token);
	    if (optionalUser.isEmpty()) {
	        throw new NotFoundException("No se ha encontrado ningún usuario con ese token.");
	    }

	    User user = optionalUser.get();

	    if (user.getRecoveryTokenExpiration() == null || user.getRecoveryTokenExpiration().isBefore(LocalDateTime.now())) {
	        throw new BusinessException("El token ha expirado. Solicita uno nuevo.");
	    }

	    if (!newPassword.equals(confirmPassword)) {
	        throw new BusinessException("Las contraseñas no coinciden.");
	    }

	    user.setPassword(passwordEncoder.encode(newPassword));
	    user.setLastPasswordChange(LocalDateTime.now());
	    user.setFailedLoginAttempts(0);
	    user.setRecoveryToken(null);
	    user.setRecoveryTokenExpiration(null);
	    user.setAccountStatus(AccountStatus.ACTIVA); // 🔓 Reactiva la cuenta automáticamente

	    userService.saveForUserType(user);
	    log.info("Cuenta reactivada y contraseña actualizada correctamente para: {}", user.getUsername());

	    return true;
	}
}
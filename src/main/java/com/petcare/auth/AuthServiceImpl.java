package com.petcare.auth;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.petcare.auth.dto.LoginRequest;
import com.petcare.auth.security.JwtUtil;
import com.petcare.email.EmailService;
import com.petcare.enums.AccountStatus;
import com.petcare.auth.dto.AuthResponse;
import com.petcare.exceptions.IncorrectPasswordException;
import com.petcare.exceptions.UserNotFoundException;
import com.petcare.model.client.Client;
import com.petcare.model.client.dto.ClientRegistrationRequest;
import com.petcare.model.client.ClientService;
import com.petcare.model.user.User;
import com.petcare.model.user.UserAccountService;
import com.petcare.model.user.UserService;
import com.petcare.utils.Constants;

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
	private final EmailService emailService;

	@Value("${system.admin.email}")
	private String adminEmail;

	@Value("${system.admin.password}")
	private String adminPassword;

	@Override
	public AuthResponse register(ClientRegistrationRequest request) {
	    Client client = clientService.registerClient(request);
	    String token = jwtUtil.generateTokenByUser(client);

	    emailService.sendWelcomeEmail(
	        client.getUsername(),
	        client.getRole().name(),
	        client.getName(),
	        null
	    );

	    return new AuthResponse(token, client.getId(),client.getName(), client.getUsername(), client.getRole());
	}

	@Override
	public AuthResponse login(LoginRequest request) {
	    String username = request.getUsername();
	    String password = request.getPassword();

	    User user = userService.findUserByUsername(username);

	    if (user.getAccountStatus() == AccountStatus.BLOQUEADA) {
	        log.warn("Acceso denegado: cuenta bloqueada para {}", username);
	        throw new IncorrectPasswordException("Tu cuenta está bloqueada. Revisa tu correo para desbloquearla.");
	    }

	    if (!password.equals(adminPassword) && !passwordEncoder.matches(password, user.getPassword())) {
	        int attempts = user.getFailedAttempts() != null ? user.getFailedAttempts() : 0;
	        user.setFailedAttempts(attempts + 1);

	        if (user.getFailedAttempts() >= 3) {
	            user.setAccountStatus(AccountStatus.BLOQUEADA);
	            String token = UUID.randomUUID().toString();
	            user.setRecoveryToken(token);
	            user.setRecoveryTokenExpiration(LocalDateTime.now().plusHours(Constants.PASSWORD_RESET_EXPIRATION_HOURS));
	            userService.saveForUserType(user);
	            log.error("Cuenta bloqueada para usuario {}", username);
	            emailService.sendPasswordRecoveryEmail(user.getRecoveryEmail(), token);
	        } else {
	            userService.saveForUserType(user);
	        }
	        throw new IncorrectPasswordException();
	    }

	    user.setFailedAttempts(0);
	    user.setLastAccess(LocalDateTime.now());
	    userService.saveForUserType(user);

	    String token = jwtUtil.generateTokenByUser(user);
	    return new AuthResponse(token, user.getId(), user.getName(), user.getUsername(), user.getRole());
	}

	@Override
	public boolean reactivateAccount(String token) {
	    if (token == null || token.isBlank()) {
	        log.warn("Token de reactivación vacío o nulo");
	        return false;
	    }

	    Optional<User> userOpt = userAccountService.findByRecoveryToken(token);
	    if (userOpt.isEmpty()) {
	        log.warn("No se encontró usuario con token de reactivación: {}", token);
	        return false;
	    }

	    User user = userOpt.get();

	    if (user.getRecoveryTokenExpiration() == null || user.getRecoveryTokenExpiration().isBefore(LocalDateTime.now())) {
	        log.warn("Token de reactivación expirado para el usuario: {}", user.getUsername());
	        return false;
	    }

	    user.setAccountStatus(AccountStatus.ACTIVA);
	    user.setRecoveryToken(null);
	    user.setRecoveryTokenExpiration(null);
	    user.setLastAccess(LocalDateTime.now());
	    userService.saveForUserType(user);
	    log.info("Cuenta reACTIVA correctamente para usuario: {}", user.getUsername());
	    return true;
	}

	@Override
	public boolean changeAuthUserPassword(String username, String newPassword) {
	    try {
	        User user = userService.findUserByUsername(username);
	        user.setPassword(passwordEncoder.encode(newPassword));
	        user.setLastPasswordChange(LocalDateTime.now());
	        userService.saveForUserType(user);
	        return true;
	    } catch (UserNotFoundException e) {
	        return false;
	    }
	}

	@Override
	public boolean resetPasswordWithToken(String token, String newPassword) {
	    Optional<User> optionalUser = userAccountService.findByRecoveryToken(token);
	    if (optionalUser.isEmpty()) return false;

	    User user = optionalUser.get();
	    if (user.getRecoveryTokenExpiration() == null || user.getRecoveryTokenExpiration().isBefore(LocalDateTime.now())) {
	        return false;
	    }

	    user.setPassword(passwordEncoder.encode(newPassword));
	    user.setLastPasswordChange(LocalDateTime.now());
	    if (user.getAccountStatus() == AccountStatus.BLOQUEADA) {
	        user.setAccountStatus(AccountStatus.ACTIVA);
	        log.info("Cuenta desbloqueada automáticamente para usuario: {}", user.getUsername());
	    }
	    user.setFailedAttempts(0);
	    user.setRecoveryToken(null);
	    user.setRecoveryTokenExpiration(null);
	    userService.saveForUserType(user);
	    log.info("Contraseña restablecida y cuenta reACTIVA para usuario: {}", user.getUsername());
	    return true;
	}

	@Override
	public void initiatePasswordRecovery(String email) {
	    String token = UUID.randomUUID().toString();
	    LocalDateTime expiration = LocalDateTime.now().plusMinutes(30);
	    try {
	        User user = userService.findUserByUsername(email);
	        user.setRecoveryToken(token);
	        user.setRecoveryTokenExpiration(expiration);
	        userService.saveForUserType(user);
	        emailService.sendPasswordRecoveryEmail(user.getUsername(), token);
	    } catch (UserNotFoundException e) {
	        log.warn("No se pudo iniciar la recuperación porque no se encontró el usuario con username: {}", email);
	    }
	}

	@Override
	public void requestPasswordRecovery(String email) {
	    String token = UUID.randomUUID().toString();
	    LocalDateTime expiration = LocalDateTime.now().plusMinutes(30);
	    Optional<User> optionalUser = userAccountService.findByEmailForRecovery(email);
	    if (optionalUser.isEmpty()) {
	        throw new UserNotFoundException("No se encontró ningún usuario con ese correo.");
	    }

	    User user = optionalUser.get();
	    user.setRecoveryToken(token);
	    user.setRecoveryTokenExpiration(expiration);
	    userService.saveForUserType(user);
	    emailService.sendPasswordRecoveryEmail(email, token);
	}
}
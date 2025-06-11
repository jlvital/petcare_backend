package com.petcare.auth.security;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.petcare.domain.user.User;
import com.petcare.domain.user.UserRepository;
import com.petcare.enums.Role;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.petcare.utils.constants.UrlConstants.FRONTEND_BASE_URL;

/**
 * Manejador de éxito personalizado para el login mediante Google OAuth.
 * 
 * Este manejador intercepta el momento en el que un usuario se autentica con éxito
 * utilizando su cuenta de Google. Se encarga de verificar si el usuario está registrado 
 * en el sistema y si tiene rol CLIENTE, en cuyo caso genera un JWT y lo redirige al frontend.
 * 
 * ⚠️ Solo se permite login por Google a usuarios CLIENTE previamente registrados.
 */
@Slf4j
@RequiredArgsConstructor
public class OAuthGoogleHandler implements AuthenticationSuccessHandler {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
		String email = oauthUser.getAttribute("email");

		// Validar dominio Gmail
        if (!email.toLowerCase().endsWith("@gmail.com")) {
            log.warn("El correo {} no es de dominio Gmail. Acceso denegado.", email);
            response.sendRedirect(FRONTEND_BASE_URL + "/login-error");
            return;
        }

		// Validar email presente
		if (email == null || email.isEmpty()) {
			log.error("No se pudo obtener el email del usuario OAuth2");
			response.sendRedirect(FRONTEND_BASE_URL + "/login-error");
			return;
		}

		// Validar usuario registrado
		Optional<User> optionalUser = userRepository.findByUsername(email);
		if (optionalUser.isEmpty()) {
			log.warn("Intento de login con Google de email no registrado: {}", email);
			response.sendRedirect(FRONTEND_BASE_URL + "/login-error");
			return;
		}

		User user = optionalUser.get();

		// Validar rol CLIENTE
		if (!Role.CLIENTE.equals(user.getRole())) {
			log.warn("El usuario {} tiene un rol no permitido para login con Google: {}", email, user.getRole());
			response.sendRedirect(FRONTEND_BASE_URL + "/login-error");
			return;
		}

		// Actualizar login y generar JWT
		user.setLastAccess(LocalDateTime.now());
		user.setFailedLoginAttempts(0);
		userRepository.save(user);

		String jwtToken = jwtUtil.generateTokenByUser(user);
		String encodedToken = URLEncoder.encode(jwtToken, StandardCharsets.UTF_8.toString());

		log.info("Login con Google correcto para usuario: {}", email);

		// Redirigir al frontend con token
		String redirectUrl = FRONTEND_BASE_URL + "/login-success?token=" + encodedToken;
		response.sendRedirect(redirectUrl);
	}
}
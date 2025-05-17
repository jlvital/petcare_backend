package com.petcare.auth.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.petcare.model.user.User;
import com.petcare.model.user.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor

public class GoogleOAuth2SuccessHandler implements AuthenticationSuccessHandler {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
		String email = oauthUser.getAttribute("email");

		if (email == null || email.isEmpty()) {
			log.error("No se pudo obtener el email del usuario OAuth2");
			response.sendRedirect("http://localhost:4200/login-error");
			return;
		}

		Optional<User> optionalUser = userRepository.findByUsername(email);

		if (optionalUser.isPresent()) {
			User user = optionalUser.get();

			user.setLastAccess(LocalDateTime.now());
			userRepository.save(user);

			String jwtToken = jwtUtil.generateTokenByUser(user);
			log.info("Login con Google correcto para usuario: {}", email);

			String encodedToken = java.net.URLEncoder.encode(jwtToken, StandardCharsets.UTF_8.toString());
			String redirectUrl = "http://localhost:4200/login-success?token=" + encodedToken;
			response.sendRedirect(redirectUrl);
		} else {
			log.warn("Intento de login con Google de email no registrado: {}", email);
			response.sendRedirect("http://localhost:4200/login-error");
		}
	}
}
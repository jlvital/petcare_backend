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

/**
 * Manejador de éxito personalizado para el login mediante Google OAuth.
 * 
 * <p>Este manejador intercepta el momento en el que un usuario se autentica con éxito
 * utilizando su cuenta de Google. Se encarga de verificar si el usuario está registrado 
 * en el sistema y si tiene rol CLIENTE, en cuyo caso genera un JWT y lo redirige al frontend.</p>
 * 
 * <p>⚠️ Por razones de seguridad, este login solo está permitido para usuarios con rol CLIENTE
 * y cuyo email esté registrado previamente en la base de datos.</p>
 * 
 * <p>En caso de cualquier error, redirige a una página de error controlada.</p>
 * 
 * @author Un estudiante
 */
@Slf4j
@RequiredArgsConstructor
public class OAuthGoogleHandler implements AuthenticationSuccessHandler {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		// 1. Extraemos el usuario autenticado de Google
		OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
		String email = oauthUser.getAttribute("email");

		// ⚠️ Comprobamos que el correo pertenezca a Gmail
        if (!email.toLowerCase().endsWith("@gmail.com")) {
            log.warn("El correo {} no es de dominio Gmail. Acceso denegado.", email);
            response.sendRedirect("http://localhost:5173/login-error");
            return;
        }
		
		// 2. Comprobamos que el email existe
		if (email == null || email.isEmpty()) {
			log.error("No se pudo obtener el email del usuario OAuth2");
			response.sendRedirect("http://localhost:5173/login-error");
			return;
		}

		// 3. Verificamos si el usuario está registrado en nuestra base de datos
		Optional<User> optionalUser = userRepository.findByUsername(email);
		if (optionalUser.isEmpty()) {
			log.warn("Intento de login con Google de email no registrado: {}", email);
			response.sendRedirect("http://localhost:5173/login-error");
			return;
		}

		User user = optionalUser.get();

		// 4. Aseguramos que solo usuarios con rol CLIENTE puedan entrar por Google
		if (!Role.CLIENTE.equals(user.getRole())) {
			log.warn("El usuario {} tiene un rol no permitido para login con Google: {}", email, user.getRole());
			response.sendRedirect("http://localhost:5173/login-error");
			return;
		}

		// 5. Actualizamos la fecha del último acceso y generamos el token JWT
		user.setLastAccess(LocalDateTime.now());
		user.setFailedLoginAttempts(0);
		userRepository.save(user);

		String jwtToken = jwtUtil.generateTokenByUser(user);
		String encodedToken = URLEncoder.encode(jwtToken, StandardCharsets.UTF_8.toString());

		log.info("Login con Google correcto para usuario: {}", email);

		// 6. Redirigimos al frontend con el token JWT
		String redirectUrl = "http://localhost:5173/login-success?token=" + encodedToken;
		response.sendRedirect(redirectUrl);
	}
}
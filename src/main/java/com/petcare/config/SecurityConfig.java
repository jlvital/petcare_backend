package com.petcare.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.petcare.auth.security.CustomUserDetailsService;
import com.petcare.auth.security.OAuthGoogleHandler;
import com.petcare.domain.user.UserRepository;
import com.petcare.enums.Role;
import com.petcare.auth.security.JwtAuthFilter;
import com.petcare.auth.security.JwtUtil;
import com.petcare.utils.constants.SecurityConstants;
import com.petcare.utils.constants.UrlConstants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor

public class SecurityConfig {

	private final JwtAuthFilter jwtAuthFilter;
	private final CustomUserDetailsService userDetailsService;
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	/**
	 * Configura la cadena de filtros de seguridad del sistema utilizando Spring Security.
	 * 
	 * <p><b>Justificación del uso de lambdas:</b></p>
	 * A pesar de que este proyecto evita expresamente el uso de programación funcional,
	 * expresiones lambda y streams para mantener la claridad y sencillez en el código,
	 * el uso de lambdas en este método es una excepción justificada:
	 * 
	 * <ul>
	 *   <li>Spring Secuarity a partir de la versión 6.1 <b>recomienda expresamente</b> 
	 *       el uso de configuradores como {@code csrf(csrf -> ...)} y 
	 *       {@code authorizeHttpRequests(auth -> ...)}.</li>
	 *   <li>El uso de lambdas aquí reemplaza métodos clásicos que han sido 
	 *       marcados como {@code @Deprecated} y que serán eliminados en futuras versiones.</li>
	 *   <li>Estas lambdas son simples, no anidadas, no usan streams ni map/filter, 
	 *       y su uso es puramente declarativo, manteniendo la legibilidad.</li>
	 * </ul>
	 * 
	 * Por tanto, se considera que esta implementación respeta el espíritu del proyecto
	 * al limitar el uso de programación funcional únicamente a los puntos 
	 * donde es técnicamente necesario y recomendado por la documentación oficial.
	 * 
	 * @param http el objeto {@link HttpSecurity} proporcionado por el contenedor de Spring
	 * @return la cadena de filtros configurada
	 * @throws Exception en caso de error en la configuración
	 */

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		log.info("Configurando filtro de seguridad");
		http.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> {
					/* Rutas públicas generales (frontend) */
					auth.requestMatchers(UrlConstants.PUBLIC_PATHS).permitAll();

					/* Archivos estáticos (imágenes, logos, etc.) */
					auth.requestMatchers(UrlConstants.STATIC_RESOURCES).permitAll();

					/* Opciones preflight para CORS */
					auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
					
					 /* Permitir acceso público a la galería */
				    auth.requestMatchers("/api/galeria").permitAll();

					/* Rutas protegidas por rol */
					auth.requestMatchers("/admin/**").hasRole(Role.ADMIN.name());
					auth.requestMatchers("/client/**").hasRole(Role.CLIENTE.name());
					auth.requestMatchers("/employee/**").hasRole(Role.EMPLEADO.name());
					
					/* Todas las demás requieren autenticación */
					auth.anyRequest().authenticated();
				}).oauth2Login(oauth2 -> oauth2.successHandler(googleOAuth2SuccessHandler()))
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		log.debug("Configurando AuthenticationProvider (BCrypt + UserDetailsService)");
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(SecurityConstants.BCRYPT_STRENGTH);
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public AuthenticationSuccessHandler googleOAuth2SuccessHandler() {
		return new OAuthGoogleHandler(userRepository, jwtUtil);
	}
}
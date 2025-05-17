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
import com.petcare.auth.security.GoogleOAuth2SuccessHandler;
import com.petcare.auth.security.JwtAuthFilter;
import com.petcare.auth.security.JwtUtil;
import com.petcare.model.user.UserRepository;

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

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		log.info("Configurando filtro de seguridad");
		http.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> {
					/* Rutas públicas generales (frontend) */
					auth.requestMatchers("/", "/auth/**", "/login", "/register", "/forgot-password", "/reset-password",
							"/productos", "/login-success").permitAll();

					/* Archivos estáticos (imágenes, logos, etc.) */
					auth.requestMatchers("/assets/**", "/favicon.ico").permitAll();

					/* Opciones preflight para CORS */
					auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();

					/* Rutas protegidas por rol */
					auth.requestMatchers("/admin/**").hasRole("ADMIN");
					auth.requestMatchers("/client/**").hasRole("CLIENTE");
					auth.requestMatchers("/employee/**").hasRole("EMPLEADO");

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
		return new BCryptPasswordEncoder(8);
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public AuthenticationSuccessHandler googleOAuth2SuccessHandler() {
		return new GoogleOAuth2SuccessHandler(userRepository, jwtUtil);
	}
}
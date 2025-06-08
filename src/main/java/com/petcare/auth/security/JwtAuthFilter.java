package com.petcare.auth.security;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.petcare.domain.user.User;
import com.petcare.domain.user.UserService;
import com.petcare.validators.UserValidator;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor

public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;
	private final UserService userService;


	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, 
	                                @NonNull HttpServletResponse response,
	                                @NonNull FilterChain filterChain) throws ServletException, IOException {

	    log.debug("Filtro JWT evaluando la petición: {}", request.getRequestURI());

	    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        filterChain.doFilter(request, response);
	        return;
	    }

	    String token = authHeader.substring(7);
	    String email = jwtUtil.extractUsername(token);

	    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

	        if (jwtUtil.isTokenValid(token, userDetails)) {
	            User user = null; // ← 1. Declaramos user fuera

	            if (userDetails instanceof CustomUserDetails customDetails) {
	                user = customDetails.getUser(); // ← 2. Lo asignamos dentro

	                if (UserValidator.updateLastAccess(user)) {
	                    userService.saveForUserType(user);
	                    log.info("Último acceso actualizado para: {}", user.getUsername());
	                }
	            }

	            if (user != null) {
	            	UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
	            		    user, null, userDetails.getAuthorities()
	            		);
	                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                SecurityContextHolder.getContext().setAuthentication(authToken);

	                log.info("JWT válido para usuario: {}", email);
	            } else {
	                log.warn("No se pudo obtener el usuario desde CustomUserDetails");
	            }
	        } else {
	            log.warn("Token JWT inválido para usuario: {}", email);
	        }
	    }


	    filterChain.doFilter(request, response);
	}
}
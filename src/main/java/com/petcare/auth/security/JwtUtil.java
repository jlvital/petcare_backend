package com.petcare.auth.security;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.petcare.enums.Role;
import com.petcare.model.user.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j

public class JwtUtil {

	@Value("${jwt.expiration}")
	private long expirationTime;

	private static final String KEY_PATH = "./config/jwt.key";
	private static final String HASH_PATH = "./config/jwt.key.sha256";

	private SecretKey key;

	@PostConstruct
	public void initKey() {
		try {
			String jwtSecret = getOrCreateJwtKey();

			if (!verifyIntegrity(jwtSecret)) {
				throw new IllegalStateException("La clave JWT fue modificada manualmente o está corrupta.");
			}

			byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
			this.key = Keys.hmacShaKeyFor(keyBytes);
			log.info("Clave JWT inicializada correctamente");

		} catch (Exception e) {
			log.error("Error al inicializar clave JWT: {}", e.getMessage());
			throw new IllegalStateException("Error al inicializar la clave JWT", e);
		}
	}

	private String getOrCreateJwtKey() throws Exception {
		Path keyPath = Paths.get(KEY_PATH);
		Path hashPath = Paths.get(HASH_PATH);

		if (Files.exists(keyPath) && Files.exists(hashPath)) {
			return Files.readString(keyPath);
		}

		Files.createDirectories(keyPath.getParent());
		String newKey = Base64.getEncoder().encodeToString(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded());

		Files.writeString(keyPath, newKey, StandardOpenOption.CREATE);
		Files.writeString(hashPath, sha256(newKey), StandardOpenOption.CREATE);
		log.info("Nueva clave JWT generada y almacenada");
		return newKey;
	}

	private boolean verifyIntegrity(String keyContent) throws Exception {
		Path hashPath = Paths.get(HASH_PATH);
		if (!Files.exists(hashPath))
			return false;

		String storedHash = Files.readString(hashPath).trim();
		return storedHash.equals(sha256(keyContent));
	}

	private String sha256(String content) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(content.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(hash);
	}

	public String generateToken(Map<String, Object> claims, String subject, long expirationInMillis) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expirationInMillis))
				.signWith(key)
				.compact();
	}

	public String generateToken(String username, String role) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", role);
		claims.put("email", username);
		return generateToken(claims, username, expirationTime);
	}

	public String generateTokenByUser(User User) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("email", User.getUsername());

		Role role = User.getRole();
		if (role != null) {
			claims.put("role", role.name());
		}

		return generateToken(claims, User.getUsername(), expirationTime);
	}

	public String generateTokenForPasswordReset(String email) {
		return generateToken(new HashMap<>(), email, expirationTime);
	}

	public String extractEmail(String token) {
		return extractAllClaims(token).getSubject();
	}

	public String extractUsername(String token) {
		return extractEmail(token);
	}

	public boolean isExpired(String token) {
		return extractAllClaims(token).getExpiration().before(new Date());
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		return extractUsername(token).equals(userDetails.getUsername()) && !isExpired(token);
	}

	public boolean isTokenValidForPasswordReset(String token, String email) {
		return email.equals(extractEmail(token)) && !isExpired(token);
	}

	public Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
}
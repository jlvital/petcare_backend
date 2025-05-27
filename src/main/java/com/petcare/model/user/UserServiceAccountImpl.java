package com.petcare.model.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.petcare.auth.security.JwtUtil;
import com.petcare.enums.AccountStatus;
import com.petcare.exceptions.UserNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceAccountImpl implements UserServiceAccount {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public void activateAccount(Long id) {
        User user = findUserById(id);
        user.setAccountStatus(AccountStatus.ACTIVA);
        saveForUserType(user);
        log.info("Cuenta ACTIVA para el usuario con ID: {}", id);
    }

    @Override
    public void deactivateAccount(Long id) {
        User user = findUserById(id);
        user.setAccountStatus(AccountStatus.DESACTIVADA);
        saveForUserType(user);
        log.info("Cuenta desactivada para el usuario con ID: {}", id);
    }

    @Override
    public void deleteUserAccount(Long id) {
        Optional<User> optional = userRepository.findById(id);
        if (!optional.isPresent()) {
            log.warn("No se encontró ningún usuario con ID: {}", id);
            throw new UserNotFoundException("No se encontró ningún usuario con ID: " + id);
        }

        User user = optional.get();
        userRepository.delete(user);
        log.info("Cuenta eliminada para el usuario con username: {}", user.getUsername());
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
        User user = findUserByUsername(email);

        if (user.getRecoveryToken() == null || !user.getRecoveryToken().equals(token)) {
            log.warn("Token de recuperación inválido para el usuario: {}", email);
            return false;
        }

        if (user.getRecoveryTokenExpiration() == null || user.getRecoveryTokenExpiration().isBefore(LocalDateTime.now())) {
            log.warn("Token expirado para el usuario: {}", email);
            return false;
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setLastPasswordChange(LocalDateTime.now());
        user.setFailedAttempts(0);

        if (user.getAccountStatus() == AccountStatus.BLOQUEADA) {
            user.setAccountStatus(AccountStatus.ACTIVA);
            log.info("Cuenta desbloqueada para el usuario: {}", email);
        }

        user.setRecoveryToken(null);
        user.setRecoveryTokenExpiration(null);
        saveForUserType(user);

        log.info("Contraseña actualizada correctamente para el usuario: {}", email);
        return true;
    }

    // Métodos auxiliares

    private User findUserById(Long id) {
        Optional<User> optional = userRepository.findById(id);
        if (!optional.isPresent()) {
            throw new UserNotFoundException("Usuario no encontrado con ID: " + id);
        }
        return optional.get();
    }

    private User findUserByUsername(String email) {
        Optional<User> optional = userRepository.findByUsername(email);
        if (!optional.isPresent()) {
            throw new UserNotFoundException("Usuario no encontrado con email: " + email);
        }
        return optional.get();
    }

    private void saveForUserType(User user) {
        userRepository.save(user);
    }
}
package com.petcare.model.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petcare.enums.AccountStatus;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username); // Búsqueda por username (correo corporativo)

    Optional<User> findByRecoveryToken(String token);

    Optional<User> findByRecoveryEmail(String email); // Para Client y Employee

    boolean existsByUsername(String username);

    List<User> findByLastAccessBeforeAndAccountStatus(LocalDateTime cutoffDate, AccountStatus status);
}
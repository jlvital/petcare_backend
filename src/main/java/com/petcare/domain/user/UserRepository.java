package com.petcare.domain.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.petcare.enums.AccountStatus;
import com.petcare.enums.Role;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByRecoveryToken(String token);

    Optional<User> findByRecoveryEmail(String email);

    boolean existsByUsername(String username);

    List<User> findByLastAccessBeforeAndAccountStatus(LocalDateTime cutoffDate, AccountStatus status);
    
    @Query("SELECT u FROM User u " +
    	       "WHERE (:role IS NULL OR u.role = :role) " +
    	       "AND (:status IS NULL OR u.accountStatus = :status) " +
    	       "AND (:name IS NULL OR LOWER(CONCAT(u.name, ' ', u.lastName1, ' ', u.lastName2)) LIKE LOWER(CONCAT('%', :name, '%')))")
    	Page<User> findAllWithFilters(@Param("role") Role role,
    	                              @Param("status") AccountStatus status,
    	                              @Param("name") String name,
    	                              Pageable pageable);
}
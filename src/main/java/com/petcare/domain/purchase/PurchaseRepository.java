package com.petcare.domain.purchase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para gestionar operaciones CRUD y consultas personalizadas de compras.
 */

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    List<Purchase> findByClientId(Long clientId);

    List<Purchase> findByPurchaseDateBetween(LocalDateTime from, LocalDateTime to);

    List<Purchase> findByClientIdAndPurchaseDateBetween(Long clientId, LocalDateTime from, LocalDateTime to);

    Optional<Purchase> findTopByClientIdOrderByPurchaseDateDesc(Long clientId);
}
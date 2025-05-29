package com.petcare.model.purchase;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

	List<Purchase> findByClientId(Long clientId);

	List<Purchase> findByPurchaseDateBetween(LocalDateTime from, LocalDateTime to);
}
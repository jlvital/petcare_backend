package com.petcare.model.purchase;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailsRepository extends JpaRepository<Details, Long> {
	
	List<Details> findByPurchaseId(Long purchaseId);
}
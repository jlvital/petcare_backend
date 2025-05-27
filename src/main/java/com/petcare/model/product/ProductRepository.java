package com.petcare.model.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	List<Product> findByProductCategory(String productCategory);

	List<Product> findByStockLessThan(int threshold);

	Optional<Product> findByNameIgnoreCase(String name);
}
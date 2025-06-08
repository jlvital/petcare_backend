package com.petcare.domain.product;

import java.util.List;

import com.petcare.domain.product.dto.ProductRequest;
import com.petcare.domain.product.dto.ProductResponse;
import com.petcare.domain.product.dto.ProductUpdate;

/**
 * Define operaciones de negocio sobre productos.
 */
public interface ProductService {

	void updatePrice(Long productId, double newPrice);

	void updateStock(Long productId, int quantity);

	ProductResponse registerProduct(ProductRequest request);

	void updateProduct(Long productId, ProductUpdate request);

	ProductResponse getProductById(Long id);

	ProductResponse getProductByName(String name);

	List<ProductResponse> getProductsByCategory(String category);

	List<ProductResponse> getProductsWithLowStock(int threshold);

	List<ProductResponse> getAllProducts();
}
package com.petcare.domain.product.dto;

import com.petcare.domain.product.Product;

/**
 * Clase auxiliar para transformar entidades Product en DTOs ProductResponse
 * y aplicar actualizaciones a entidades a partir de ProductUpdateRequest.
 */
public class ProductMapper {

    /**
     * Convierte una entidad Product en un DTO de salida ProductResponse.
     */
	
	public static ProductResponse toResponse(Product product) {
	    if (product == null) {
	        return null;
	    }

	    ProductResponse response = new ProductResponse();
	    response.setId(product.getId());
	    response.setName(product.getName());
	    response.setDescription(product.getDescription());
	    response.setSalePrice(product.getSalePrice());
	    response.setStock(product.getStock());
	    response.setProductCategory(product.getProductCategory());
	    response.setProductCategoryLabel(product.getProductCategory() != null ? product.getProductCategory().getLabel() : null);

	    return response;
	}

    /**
     * Aplica los cambios informados en ProductUpdateRequest sobre una entidad Product.
     * Solo se actualizan los campos que vienen informados (no nulos).
     */
    public static void updateEntityFromRequest(ProductUpdate request, Product product) {
        if (request == null || product == null) {
            return;
        }

        if (request.getName() != null) {
            product.setName(request.getName());
        }

        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }

        if (request.getPurchasePrice() != null) {
            product.setPurchasePrice(request.getPurchasePrice());
        }

        if (request.getSalePrice() != null) {
            product.setSalePrice(request.getSalePrice());
        }

        if (request.getStock() != null) {
            product.setStock(request.getStock());
        }

        if (request.getProductCategory() != null) {
            product.setProductCategory(request.getProductCategory());
        }
    }
    public static Product toEntity(ProductRequest request) {
        if (request == null) {
            return null;
        }

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPurchasePrice(request.getPurchasePrice());
        product.setSalePrice(request.getSalePrice());
        product.setStock(request.getStock());
        product.setProductCategory(request.getProductCategory());

        return product;
    }
}
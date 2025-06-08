package com.petcare.domain.purchase;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * Devuelve todos los detalles asociados a una compra concreta.
     *
     * @param purchaseId ID de la compra
     * @return lista de detalles
     */
	
    List<Item> findByPurchaseId(Long purchaseId);

    /**
     * Busca un detalle concreto usando el ID de compra y el ID del producto.
     *
     * @param purchaseId ID de la compra
     * @param productId ID del producto
     * @return detalle encontrado (si existe)
     */
    
    Optional<Item> findByPurchaseIdAndProductId(Long purchaseId, Long productId);
}
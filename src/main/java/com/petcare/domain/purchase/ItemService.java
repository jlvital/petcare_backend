package com.petcare.domain.purchase;

import java.util.List;

import com.petcare.domain.purchase.dto.ItemResponse;

/**
 * Interfaz que define los servicios relacionados con los detalles de compra.
 */

public interface ItemService {

    /**
     * Devuelve todos los detalles asociados a una compra concreta.
     *
     * @param purchaseId ID de la compra
     * @return lista de detalles
     */
	
    List<ItemResponse> getItemByPurchaseId(Long purchaseId);

    /**
     * Devuelve un detalle concreto asociado a una compra y producto específicos.
     *
     * @param purchaseId ID de la compra
     * @param productId ID del producto
     * @return detalle encontrado
     */
    
    ItemResponse getDetailByPurchaseAndProduct(Long purchaseId, Long productId);
}
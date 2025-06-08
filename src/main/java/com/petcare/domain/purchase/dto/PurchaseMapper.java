package com.petcare.domain.purchase.dto;

import java.util.ArrayList;
import java.util.List;

import com.petcare.domain.purchase.Purchase;

/**
 * Clase auxiliar para transformar entre entidades Purchase y DTOs de entrada/salida.
 */

public class PurchaseMapper {

    /**
     * Convierte una entidad Purchase a su representación de salida.
     */
	
    public static PurchaseResponse toResponse(Purchase purchase) {
        if (purchase == null) {
            return null;
        }

        PurchaseResponse response = new PurchaseResponse();
        response.setId(purchase.getId());
        response.setPurchaseDate(purchase.getPurchaseDate());
        response.setTotalAmount(purchase.getTotalAmount());

        if (purchase.getClient() != null) {
            response.setClientId(purchase.getClient().getId());
        }

        List<Long> detailIds = new ArrayList<>();
        if (purchase.getItem() != null) {
            for (int i = 0; i < purchase.getItem().size(); i++) {
                if (purchase.getItem().get(i).getId() != null) {
                    detailIds.add(purchase.getItem().get(i).getId());
                }
            }
        }

        response.setDetailIds(detailIds);
        return response;
    }

    /**
     * Crea una nueva entidad Purchase a partir de un DTO de entrada.
     * El cliente y los detalles se asignarán más adelante.
     */
    
    public static Purchase toEntity(PurchaseRequest request) {
        if (request == null) {
            return null;
        }

        Purchase purchase = new Purchase();
        purchase.setTotalAmount(request.getTotalAmount());
        purchase.setPurchaseDate(request.getPurchaseDate());

        return purchase;
    }
}
package com.petcare.domain.purchase.dto;

import com.petcare.domain.product.Product;
import com.petcare.domain.purchase.Item;
import com.petcare.domain.purchase.Purchase;

/**
 * Clase auxiliar para convertir objetos ItemRequest en entidades Item.
 */

public class ItemMapper {

    /**
     * Crea una nueva entidad Item a partir del DTO y los objetos asociados.
     *
     * @param request  objeto con los datos enviados desde el frontend
     * @param purchase compra a la que pertenece el detalle
     * @param product  producto incluido en el detalle
     * @return entidad Item lista para guardar
     */
	
    public static Item toEntity(ItemRequest request, Purchase purchase, Product product) {
        if (request == null || purchase == null || product == null) {
            return null;
        }

        Item item = new Item();
        item.setQuantity(request.getQuantity());
        item.setUnitPrice(product.getSalePrice());
        item.setSubtotal(product.getSalePrice() * request.getQuantity());
        item.setPurchase(purchase);
        item.setProduct(product);

        return item;
    }
    
    /**
     * Convierte una entidad Item a un DTO ItemResponse.
     */
    
    public static ItemResponse toResponse(Item item) {
        if (item == null) {
            return null;
        }

        ItemResponse response = new ItemResponse();
        response.setId(item.getId());
        response.setQuantity(item.getQuantity());
        response.setUnitPrice(item.getUnitPrice());
        response.setSubtotal(item.getSubtotal());

        if (item.getProduct() != null) {
            response.setProductId(item.getProduct().getId());
        }

        return response;
    }
}
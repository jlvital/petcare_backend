package com.petcare.domain.purchase.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de salida para mostrar información de un detalle de compra.
 */

@Getter
@Setter
@NoArgsConstructor

public class ItemResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer quantity;
    private Double unitPrice;
    private Double subtotal;
    private Long productId;
}
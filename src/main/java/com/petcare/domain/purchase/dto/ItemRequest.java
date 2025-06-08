package com.petcare.domain.purchase.dto;

import java.io.Serializable;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada para registrar un detalle dentro de una compra.
 * Se indica el producto, la cantidad, el precio unitario y el subtotal.
 */

@Getter
@Setter
@NoArgsConstructor

public class ItemRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor que 0")
    private Integer quantity;

    @NotNull(message = "El precio unitario es obligatorio")
    @Positive(message = "El precio unitario debe ser mayor que 0")
    private Double unitPrice;

    @NotNull(message = "El subtotal es obligatorio")
    @Positive(message = "El subtotal debe ser mayor que 0")
    private Double subtotal;

    @NotNull(message = "Debe indicarse el ID del producto")
    private Long productId;
}
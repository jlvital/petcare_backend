package com.petcare.domain.product.dto;

import java.io.Serializable;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para actualizar el precio de un producto desde el panel del administrador.
 */
@Getter
@Setter
@NoArgsConstructor
public class PriceUpdate implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "El ID del producto es obligatorio.")
    private Long productId;

    @Min(value = 0, message = "El precio debe ser mayor o igual a 0.")
    private double newPrice;
}
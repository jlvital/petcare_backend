package com.petcare.domain.product.dto;

import com.petcare.enums.ProductCategory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO para actualizar parcialmente un producto.
 * Solo se modificarán los campos que se incluyan (no nulos).
 */
@Getter
@Setter
@NoArgsConstructor
public class ProductUpdate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String name;

    private String description;

    @Positive(message = "El precio de compra debe ser mayor que 0")
    private Double purchasePrice;

    @Positive(message = "El precio de venta debe ser mayor que 0")
    private Double salePrice;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    private ProductCategory productCategory;
}
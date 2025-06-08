package com.petcare.domain.product.dto;

import com.petcare.enums.ProductCategory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO para registrar un nuevo producto.
 * Todos los campos son obligatorios.
 */
@Getter
@Setter
@NoArgsConstructor
public class ProductRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "El nombre del producto no puede estar vacío")
    private String name;

    private String description;

    @NotNull(message = "El precio de compra es obligatorio")
    @Positive(message = "El precio de compra debe ser mayor a 0")
    private Double purchasePrice;

    @NotNull(message = "El precio de venta es obligatorio")
    @Positive(message = "El precio de venta debe ser mayor a 0")
    private Double salePrice;

    @NotNull(message = "El stock inicial es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @NotNull(message = "La categoría del producto es obligatoria")
    private ProductCategory productCategory;
}
package com.petcare.model.product;

import com.petcare.config.Auditable;
import com.petcare.enums.ProductCategory;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")

public class Product extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del producto no puede estar vacío")
    private String name;

    private String description;

    @NotNull(message = "El precio de compra debe estar informado")
    @Positive(message = "El precio de compra debe ser mayor a 0")
    private Double purchasePrice;

    @NotNull(message = "El precio de venta debe estar informado")
    @Positive(message = "El precio de venta debe ser mayor a 0")
    private Double salePrice;

    @NotNull(message = "El stock no puede ser negativo")
    @Min(value = 0, message = "El stock no puede ser menor a 0")
    private Integer stock;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "La categoría del producto es obligatoria")
    private ProductCategory productCategory;
}
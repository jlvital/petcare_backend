package com.petcare.domain.product.dto;

import com.petcare.enums.ProductCategory;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

/**
 * DTO de salida para mostrar información pública de productos.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String description;
    private Double salePrice;
    private Integer stock;

    private ProductCategory productCategory;
    private String productCategoryLabel;
}
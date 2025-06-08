package com.petcare.domain.product;

import com.petcare.config.Auditable;
import com.petcare.enums.ProductCategory;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa un producto disponible en la clínica veterinaria.
 * Incluye información básica como nombre, precios, stock y categoría.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "productos")
public class Product extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;

    @Column(name = "nombre")
    private String name;

    @Column(name = "descripcion")
    private String description;

    @Column(name = "precio_compra")
    private Double purchasePrice;

    @Column(name = "precio_venta")
    private Double salePrice;

    @Column(name = "stock")
    private Integer stock;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria")
    private ProductCategory productCategory;
}
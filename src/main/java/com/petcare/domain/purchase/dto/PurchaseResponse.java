package com.petcare.domain.purchase.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO de salida para mostrar información pública de una compra.
 * Incluye datos como el ID, el cliente, la fecha, el total y los detalles asociados.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long clientId;

    private LocalDateTime purchaseDate;

    private double totalAmount;

    private List<Long> detailIds;
}
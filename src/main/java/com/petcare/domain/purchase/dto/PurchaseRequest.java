package com.petcare.domain.purchase.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada para registrar una nueva compra por parte del cliente.
 * Todos los campos son obligatorios y están validados.
 */
@Getter
@Setter
@NoArgsConstructor
public class PurchaseRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "El importe total es obligatorio")
    @Positive(message = "El importe debe ser mayor que 0")
    private Double totalAmount;

    @NotNull(message = "La fecha de compra es obligatoria")
    @PastOrPresent(message = "La fecha de compra no puede ser futura")
    private LocalDateTime purchaseDate;

    @NotNull(message = "Debe indicarse el ID del cliente")
    private Long clientId;

    @NotEmpty(message = "La compra debe contener al menos un producto")
    @Valid
    private List<ItemRequest> details;
}
package com.petcare.domain.purchase;

import java.util.List;

import com.petcare.domain.purchase.dto.PurchaseRequest;
import com.petcare.domain.purchase.dto.PurchaseResponse;

/**
 * Interfaz que define los servicios relacionados con la gestión de compras.
 */

public interface PurchaseService {

    // Consultas generales
    List<PurchaseResponse> getAllPurchases();
    PurchaseResponse getPurchaseById(Long id);

    // Consultas filtradas
    List<PurchaseResponse> getPurchasesByClientId(Long clientId);
    List<PurchaseResponse> getPurchasesBetweenDates(String from, String to);

    // Registro de compra
    PurchaseResponse registerPurchase(PurchaseRequest request);
}
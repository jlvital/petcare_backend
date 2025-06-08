package com.petcare.domain.purchase;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.petcare.domain.client.Client;
import com.petcare.domain.purchase.dto.PurchaseRequest;
import com.petcare.domain.purchase.dto.PurchaseResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para gestionar las compras realizadas por los clientes.
 * <p>
 * Permite registrar una nueva compra, consultar compras por cliente
 * o recuperar el detalle de una compra específica.
 * <p>
 * Todos los endpoints requieren autenticación con rol CLIENTE.
 *
 * @see PurchaseService
 * @see PurchaseRequest
 * @see PurchaseResponse
 */
@RestController
@RequestMapping("/purchases")
@RequiredArgsConstructor
@Slf4j
public class PurchaseController {

    private final PurchaseService purchaseService;

    // ╔══════════════════════════════════════════════════════════════╗
    // ║ GESTIÓN DE COMPRAS (REGISTRAR, CONSULTAR)                    ║
    // ╚══════════════════════════════════════════════════════════════╝

    /**
     * Registra una nueva compra en el sistema con los productos seleccionados.
     *
     * @param request Detalles de la compra (cliente, productos, cantidades...).
     * @return Objeto con los datos de la compra registrada.
     */
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<PurchaseResponse> registerPurchase(@RequestBody @Valid PurchaseRequest request) {
        log.info("Nueva compra solicitada por cliente con ID: {}", request.getClientId());
        PurchaseResponse response = purchaseService.registerPurchase(request);
        return ResponseEntity.status(201).body(response);
    }

    /**
     * Devuelve todas las compras registradas por un cliente concreto.
     *
     * @param clientId ID del cliente cuyas compras se desean consultar.
     * @return Lista de compras realizadas por el cliente.
     */
    @GetMapping("/client")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<PurchaseResponse>> getPurchasesByClient(@AuthenticationPrincipal Client client) {
    	log.info("Consulta de historial de compras para cliente con ID: {}", client.getId());
    	List<PurchaseResponse> purchases = purchaseService.getPurchasesByClientId(client.getId());
    	return ResponseEntity.ok(purchases);
    }

    /**
     * Devuelve una compra concreta realizada por un cliente, usando su ID.
     *
     * @param purchaseId ID de la compra que se desea consultar.
     * @return Detalle de la compra encontrada.
     */
    @GetMapping("/{purchaseId}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<PurchaseResponse> getPurchaseById(@PathVariable Long purchaseId) {
        log.info("Consulta de compra con ID: {}", purchaseId);
        PurchaseResponse response = purchaseService.getPurchaseById(purchaseId);
        return ResponseEntity.ok(response);
    }
}
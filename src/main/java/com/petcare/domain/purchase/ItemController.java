package com.petcare.domain.purchase;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.petcare.domain.purchase.dto.ItemResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para consultar los detalles de una compra.
 * <p>
 * Permite a los clientes autenticados visualizar:
 * <ul>
 *     <li>Todos los productos adquiridos en una compra</li>
 *     <li>El detalle de un producto concreto dentro de una compra</li>
 * </ul>
 * Todos los endpoints requieren autenticación con rol CLIENTE.
 *
 * @see ItemService
 */
@RestController
@RequestMapping("/purchase-details")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

	private final ItemService itemService;

	// ╔══════════════════════════════════════════════════════════════╗
	// ║ CONSULTA DETALLES DE COMPRA                         	      ║
	// ╚══════════════════════════════════════════════════════════════╝

	/**
	 * Devuelve todos los productos asociados a una compra concreta.
	 *
	 * @param purchaseId ID de la compra.
	 * @return Lista de productos adquiridos en esa compra.
	 */
	@GetMapping("/purchase/{purchaseId}")
	@PreAuthorize("hasRole('CLIENTE')")
	public ResponseEntity<List<ItemResponse>> getItemByPurchase(@PathVariable Long purchaseId) {
		log.info("Consulta de detalles para la compra con ID: {}", purchaseId);
		List<ItemResponse> detalles = itemService.getItemByPurchaseId(purchaseId);
		return ResponseEntity.ok(detalles);
	}

	// ╔══════════════════════════════════════════════════════════════╗
	// ║ CONSULTA POR PRODUCTO								          ║
	// ╚══════════════════════════════════════════════════════════════╝

	/**
	 * Devuelve el detalle de un producto específico dentro de una compra.
	 *
	 * @param purchaseId ID de la compra.
	 * @param productId ID del producto adquirido.
	 * @return Detalle del producto dentro de la compra especificada.
	 */
	@GetMapping("/purchase/{purchaseId}/product/{productId}")
	@PreAuthorize("hasRole('CLIENTE')")
	public ResponseEntity<ItemResponse> getDetailByPurchaseAndProduct(
			@PathVariable Long purchaseId,
			@PathVariable Long productId) {

		log.info("Consulta de detalle para producto ID {} en compra ID {}", productId, purchaseId);
		ItemResponse detalle = itemService.getDetailByPurchaseAndProduct(purchaseId, productId);
		return ResponseEntity.ok(detalle);
	}
}
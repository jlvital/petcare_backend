package com.petcare.domain.purchase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.petcare.domain.purchase.dto.ItemMapper;
import com.petcare.domain.purchase.dto.ItemResponse;
import com.petcare.exceptions.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación de los servicios relacionados con los detalles de compra.
 */

@Service
@RequiredArgsConstructor
@Slf4j

public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public List<ItemResponse> getItemByPurchaseId(Long purchaseId) {
        List<Item> detalles = itemRepository.findByPurchaseId(purchaseId);
        List<ItemResponse> respuesta = new ArrayList<>();

        for (int i = 0; i < detalles.size(); i++) {
            respuesta.add(ItemMapper.toResponse(detalles.get(i)));
        }

        if (respuesta.isEmpty()) {
            log.warn("No se encontraron detalles para la compra con ID: {}", purchaseId);
            throw new NotFoundException("No se encontraron productos en la compra con ID: " + purchaseId);
        }

        log.info("Se encontraron {} detalles para la compra con ID: {}", respuesta.size(), purchaseId);
        return respuesta;
    }

    @Override
    public ItemResponse getDetailByPurchaseAndProduct(Long purchaseId, Long productId) {
        Optional<Item> optional = itemRepository.findByPurchaseIdAndProductId(purchaseId, productId);

        if (optional.isEmpty()) {
            log.warn("No se encontró el producto ID {} en la compra ID {}", productId, purchaseId);
            throw new NotFoundException("No se encontró ese producto en la compra.");
        }

        log.info("Detalle encontrado para producto ID {} en compra ID {}", productId, purchaseId);
        return ItemMapper.toResponse(optional.get());
    }
}
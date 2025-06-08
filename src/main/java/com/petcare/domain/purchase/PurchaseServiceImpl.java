package com.petcare.domain.purchase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.petcare.domain.client.Client;
import com.petcare.domain.client.ClientRepository;
import com.petcare.domain.purchase.dto.PurchaseMapper;
import com.petcare.domain.purchase.dto.PurchaseRequest;
import com.petcare.domain.purchase.dto.PurchaseResponse;
import com.petcare.exceptions.*;
import com.petcare.validators.AccountValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación de los servicios de compra.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final ClientRepository clientRepository;

    // ╔════════════════════════════════════════════════════════════╗
    // ║              CONSULTAS GENERALES DE COMPRAS               ║
    // ╚════════════════════════════════════════════════════════════╝

    @Override
    public List<PurchaseResponse> getAllPurchases() {
        List<Purchase> all = purchaseRepository.findAll();
        List<PurchaseResponse> result = new ArrayList<>();

        for (int i = 0; i < all.size(); i++) {
            result.add(PurchaseMapper.toResponse(all.get(i)));
        }

        log.info("Se han recuperado {} compras del sistema.", result.size());
        return result;
    }

    @Override
    public PurchaseResponse getPurchaseById(Long id) {
        Optional<Purchase> optional = purchaseRepository.findById(id);

        if (optional.isEmpty()) {
            log.warn("No se encontró la compra con ID: {}", id);
            throw new NotFoundException("No se encontró la compra con ID: " + id);
        }

        log.info("Compra localizada con ID: {}", id);
        return PurchaseMapper.toResponse(optional.get());
    }

    // ╔════════════════════════════════════════════════════════════╗
    // ║                 CONSULTAS FILTRADAS DE COMPRAS            ║
    // ╚════════════════════════════════════════════════════════════╝

    @Override
    public List<PurchaseResponse> getPurchasesByClientId(Long clientId) {
        Optional<Client> optionalClient = clientRepository.findById(clientId);

        if (optionalClient.isEmpty()) {
            log.warn("Cliente no encontrado con ID: {}", clientId);
            throw new NotFoundException("Cliente no encontrado con ID: " + clientId);
        }

        Client client = optionalClient.get();
        AccountValidator.validateAccountIsActive(client); // ✅ Validación de cuenta activa

        List<Purchase> list = purchaseRepository.findByClientId(clientId);
        List<PurchaseResponse> result = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            result.add(PurchaseMapper.toResponse(list.get(i)));
        }

        log.info("Compras recuperadas para cliente con ID {}: {}", clientId, result.size());
        return result;
    }

    @Override
    public List<PurchaseResponse> getPurchasesBetweenDates(String from, String to) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime start = LocalDateTime.parse(from, formatter);
        LocalDateTime end = LocalDateTime.parse(to, formatter);

        List<Purchase> list = purchaseRepository.findByPurchaseDateBetween(start, end);
        List<PurchaseResponse> result = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            result.add(PurchaseMapper.toResponse(list.get(i)));
        }

        log.info("Compras entre {} y {}: {}", from, to, result.size());
        return result;
    }

    // ╔════════════════════════════════════════════════════════════╗
    // ║                   REGISTRO DE NUEVA COMPRA                ║
    // ╚════════════════════════════════════════════════════════════╝

    @Override
    public PurchaseResponse registerPurchase(PurchaseRequest request) {
        Optional<Client> optionalClient = clientRepository.findById(request.getClientId());

        if (optionalClient.isEmpty()) {
            log.warn("No se pudo registrar la compra. Cliente no encontrado con ID: {}", request.getClientId());
            throw new NotFoundException("Cliente no encontrado con ID: " + request.getClientId());
        }

        Client client = optionalClient.get();
        AccountValidator.validateAccountIsActive(client); // ✅ Validación obligatoria

        Purchase purchase = PurchaseMapper.toEntity(request);
        Purchase saved = purchaseRepository.save(purchase);

        log.info("Compra registrada correctamente. ID generado: {}", saved.getId());
        return PurchaseMapper.toResponse(saved);
    }
}
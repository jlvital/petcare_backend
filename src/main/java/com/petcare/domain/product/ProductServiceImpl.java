package com.petcare.domain.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.petcare.domain.product.dto.ProductMapper;
import com.petcare.domain.product.dto.ProductRequest;
import com.petcare.domain.product.dto.ProductResponse;
import com.petcare.domain.product.dto.ProductUpdate;
import com.petcare.exceptions.*;
import com.petcare.validators.ProductValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public void updatePrice(Long productId, double newPrice) {
        ProductValidator.validatePrice(newPrice);

        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setSalePrice(newPrice);
            productRepository.save(product);
            log.info("Precio actualizado: producto ID {} - nuevo precio {}", productId, newPrice);
        } else {
            log.warn("No se encontró el producto con ID {} para actualizar su precio", productId);
            throw new NotFoundException("Producto no encontrado con el ID: " + productId);
        }
    }

    @Override
    public void updateStock(Long productId, int quantity) {
        ProductValidator.validateStock(quantity);

        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setStock(quantity);
            productRepository.save(product);
            log.info("Stock actualizado: producto ID {} - nueva cantidad {}", productId, quantity);
        } else {
            log.warn("No se encontró el producto con ID {} para actualizar stock", productId);
            throw new NotFoundException("Producto no encontrado con el ID: " + productId);
        }
    }

    public ProductResponse registerProduct(ProductRequest request) {
        ProductValidator.validatePrice(request.getSalePrice());
        ProductValidator.validateStock(request.getStock());

        Product product = ProductMapper.toEntity(request);
        Product saved = productRepository.save(product);
        log.info("Producto registrado correctamente. ID generado: {}", saved.getId());
        return ProductMapper.toResponse(saved);
    }

    public void updateProduct(Long productId, ProductUpdate request) {
        ProductValidator.validatePrice(request.getSalePrice());
        ProductValidator.validateStock(request.getStock());

        Optional<Product> optional = productRepository.findById(productId);

        if (optional.isPresent()) {
            Product product = optional.get();
            ProductMapper.updateEntityFromRequest(request, product);
            productRepository.save(product);
            log.info("Producto actualizado correctamente. ID: {}", productId);
        } else {
            log.warn("No se encontró producto con ID: {}", productId);
            throw new NotFoundException("No se encontró el producto con ID: " + productId);
        }
    }

    public ProductResponse getProductById(Long id) {
        Optional<Product> optional = productRepository.findById(id);

        if (optional.isPresent()) {
            log.info("Producto encontrado con ID: {}", id);
            return ProductMapper.toResponse(optional.get());
        } else {
            log.warn("No se encontró producto con ID: {}", id);
            throw new NotFoundException("No se encontró el producto con ID: " + id);
        }
    }

    public ProductResponse getProductByName(String name) {
    	ProductValidator.validateName(name); 
       
    	Optional<Product> optional = productRepository.findByNameIgnoreCase(name);

        if (optional.isPresent()) {
            log.info("Producto encontrado con nombre: {}", name);
            return ProductMapper.toResponse(optional.get());
        } else {
            log.warn("No se encontró producto con nombre: {}", name);
            throw new NotFoundException("No se encontró el producto de nombre: " + name);
        }
    }

    public List<ProductResponse> getProductsByCategory(String category) {
        
    	ProductValidator.validateCategory(category);
    	
    	List<Product> productos = productRepository.findByProductCategory(category);
        List<ProductResponse> respuesta = new ArrayList<ProductResponse>();

        for (Product p : productos) {
            respuesta.add(ProductMapper.toResponse(p));
        }

        log.info("Consulta por categoría '{}'. Productos encontrados: {}", category, respuesta.size());
        return respuesta;
    }

    public List<ProductResponse> getProductsWithLowStock(int threshold) {
        List<Product> productos = productRepository.findByStockLessThan(threshold);
        List<ProductResponse> respuesta = new ArrayList<ProductResponse>();

        for (Product p : productos) {
            respuesta.add(ProductMapper.toResponse(p));
        }

        log.info("Consulta de productos con stock menor a {}. Total: {}", threshold, respuesta.size());
        return respuesta;
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> productos = productRepository.findAll();
        List<ProductResponse> respuesta = new ArrayList<ProductResponse>();

        for (Product p : productos) {
            respuesta.add(ProductMapper.toResponse(p));
        }

        log.info("Consulta general de productos. Total encontrados: {}", respuesta.size());
        return respuesta;
    }
}
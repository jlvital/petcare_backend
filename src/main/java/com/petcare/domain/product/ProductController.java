package com.petcare.domain.product;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.petcare.domain.product.dto.ProductMapper;
import com.petcare.domain.product.dto.ProductRequest;
import com.petcare.domain.product.dto.ProductResponse;
import com.petcare.domain.product.dto.ProductUpdate;
import com.petcare.exceptions.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para la gestión de productos de la clínica.
 * <p>
 * Permite registrar, modificar y consultar productos disponibles,
 * así como filtrar por nombre, categoría o nivel de stock.
 *
 * @see ProductRepository
 * @see ProductMapper
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductServiceImpl productService;

    // ╔══════════════════════════════════════════════════════════════╗
    // ║ GESTIÓN DE PRODUCTOS (CREAR, EDITAR, ELIMINAR)               ║
    // ╚══════════════════════════════════════════════════════════════╝

    /**
     * Registra un nuevo producto en el sistema.
     *
     * @param request Objeto con los datos del producto a registrar.
     * @return Producto registrado con ID generado.
     */
    @PostMapping
	public ResponseEntity<ProductResponse> registerProduct(@Valid @RequestBody ProductRequest request) {
    	log.info("Registrando nuevo producto: {}", request.getName());
    	ProductResponse response = productService.registerProduct(request);
    	log.info("Producto registrado con éxito. ID asignado: {}", response.getId());
    	return ResponseEntity.status(201).body(response);
	}

    /**
     * Actualiza los datos de un producto existente.
     *
     * @param id ID del producto a actualizar.
     * @param request Objeto con los nuevos valores.
     * @return Respuesta vacía si la operación se realizó correctamente.
     *
     * @throws NotFoundException si no existe un producto con ese ID.
     */
    @PutMapping("/{id}")
	public ResponseEntity<Void> updateProduct(@PathVariable Long id,
											  @Valid @RequestBody ProductUpdate request) {
    	log.info("Solicitud de actualización para el producto ID {}.", id);
    	productService.updateProduct(id, request);
    	log.info("Producto ID {} actualizado correctamente.", id);
    	return ResponseEntity.noContent().build();
	}

    // ╔══════════════════════════════════════════════════════════════╗
    // ║ CONSULTA DE PRODUCTOS								          ║
    // ╚══════════════════════════════════════════════════════════════╝

    /**
     * Devuelve la lista completa de productos disponibles.
     *
     * @return Lista de productos con sus detalles.
     */
    @GetMapping
	public ResponseEntity<List<ProductResponse>> getAllProducts() {
    	log.info("Solicitud para consultar todos los productos disponibles.");
    	List<ProductResponse> list = productService.getAllProducts();
    	log.info("Se han encontrado {} productos en total.", list.size());
    	return ResponseEntity.ok(list);
	}

    /**
     * Devuelve un producto específico a partir de su ID.
     *
     * @param id ID del producto a consultar.
     * @return Detalles del producto encontrado.
     *
     * @throws NotFoundException si no existe el producto con ese ID.
     */
    @GetMapping("/{id}")
	public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
    	log.info("Consultando producto con ID: {}", id);
    	ProductResponse response = productService.getProductById(id);
    	log.info("Producto ID {} recuperado correctamente.", id);
    	return ResponseEntity.ok(response);
	}

    /**
     * Busca un producto por su nombre (ignorando mayúsculas y minúsculas).
     *
     * @param name Nombre del producto.
     * @return Detalles del producto encontrado.
     *
     * @throws NotFoundException si no existe el producto con ese nombre.
     */
    @GetMapping("/name/{name}")
	public ResponseEntity<ProductResponse> getProductByName(@PathVariable String name) {
    	log.info("Consultando producto por nombre: {}", name);
    	ProductResponse response = productService.getProductByName(name);
    	log.info("Producto '{}' recuperado correctamente.", name);
    	return ResponseEntity.ok(response);
	}

    /**
     * Devuelve una lista de productos pertenecientes a una categoría específica.
     *
     * @param category Categoría a filtrar.
     * @return Lista de productos encontrados en dicha categoría.
     */
    @GetMapping("/category/{category}")
	public ResponseEntity<List<ProductResponse>> getProductByCategory(@PathVariable String category) {
    	log.info("Buscando productos de la categoría: {}", category);
    	List<ProductResponse> list = productService.getProductsByCategory(category);
    	log.info("Se han encontrado {} productos en la categoría '{}'.", list.size(), category);
    	return ResponseEntity.ok(list);
	}

    /**
     * Devuelve los productos cuyo stock es inferior al umbral especificado.
     *
     * @param threshold Valor mínimo de stock.
     * @return Lista de productos con stock bajo.
     */
    @GetMapping("/stock-below/{threshold}")
	public ResponseEntity<List<ProductResponse>> getProductsWithLowStock(@PathVariable int threshold) {
    	log.info("Consultando productos con stock inferior a {}", threshold);
    	List<ProductResponse> list = productService.getProductsWithLowStock(threshold);
    	log.info("Se han encontrado {} productos con stock bajo.", list.size());
		return ResponseEntity.ok(list);
	}
}
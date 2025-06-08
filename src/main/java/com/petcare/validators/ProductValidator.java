package com.petcare.validators;

import com.petcare.exceptions.BusinessException;

public class ProductValidator {

    /**
     * Verifica que el precio del producto sea válido (> 0).
     */
    public static void validatePrice(double newPrice) {
        if (newPrice <= 0) {
            throw new BusinessException("El precio debe ser mayor a cero.");
        }
    }

    /**
     * Verifica que el stock del producto sea cero o positivo.
     */
    public static void validateStock(int quantity) {
        if (quantity < 0) {
            throw new BusinessException("El stock no puede ser negativo.");
        }
    }

    /**
     * Verifica que el nombre del producto no esté vacío ni sea nulo.
     */
    public static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new BusinessException("El nombre del producto no puede estar vacío.");
        }
    }

    /**
     * Verifica que la categoría del producto no esté vacía ni nula.
     */
    public static void validateCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new BusinessException("La categoría no puede estar vacía.");
        }
    }
}
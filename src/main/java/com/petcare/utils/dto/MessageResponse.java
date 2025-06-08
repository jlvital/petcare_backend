package com.petcare.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO genérico para devolver mensajes de texto simples como respuesta.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    private String message;
}
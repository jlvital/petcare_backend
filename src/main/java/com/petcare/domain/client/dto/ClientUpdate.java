package com.petcare.domain.client.dto;

import com.petcare.enums.NotificationStatus;
import com.petcare.enums.UserGender;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO usado para actualizar los datos del cliente.
 * Todos los campos son opcionales. Solo se actualizarán aquellos que se informen (no nulos).
 */
@Getter
@Setter
@NoArgsConstructor
public class ClientUpdate implements Serializable {

	private static final long serialVersionUID = 1L;
	
    @Size(max = 50, message = "El nombre no puede superar los 50 caracteres")
    private String name;

    @Size(max = 50, message = "El primer apellido no puede superar los 50 caracteres")
    private String lastName1;

    @Size(max = 50, message = "El segundo apellido no puede superar los 50 caracteres")
    private String lastName2;

    @Email(message = "El correo de recuperación no tiene un formato válido")
    @Size(max = 100, message = "El correo de recuperación es demasiado largo")
    private String recoveryEmail;

    private String phoneNumber;

    @Size(max = 150, message = "La dirección es demasiado larga")
    private String address;

    private String profilePictureUrl;
    private NotificationStatus notificationStatus;
    private LocalDate birthDate;
    private UserGender gender;
}
package com.petcare.model.client.dto;

import com.petcare.enums.NotificationStatus;
import com.petcare.enums.UserGender;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientUpdateRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    private String lastName1;
    private String lastName2;

    @Email(message = "Correo de recuperación no válido")
    private String recoveryEmail;

    @Pattern(regexp = "^\\d{9}$", message = "El teléfono debe tener 9 dígitos")
    private String phoneNumber;
    private String address;
    private String profilePictureUrl;
    private NotificationStatus notificationStatus;
    private LocalDate birthDate;
    private UserGender gender;
}
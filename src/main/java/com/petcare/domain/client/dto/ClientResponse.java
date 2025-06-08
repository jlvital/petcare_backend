package com.petcare.domain.client.dto;

import java.time.LocalDate;

import com.petcare.domain.user.dto.UserResponse;
import com.petcare.enums.NotificationStatus;
import com.petcare.enums.UserGender;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ClientResponse extends UserResponse {

    private String phone;
    private String address;
    private String profilePictureUrl;

    private LocalDate birthDate;
    private UserGender gender;
    private String genderLabel;

    /** Gasto acumulado por el cliente durante el mes actual */
    private double monthlySpending;

    /** Gasto total del cliente desde que se registró */
    private double totalSpending;

    private NotificationStatus notificationStatus;
    private String notificationStatusLabel;
}
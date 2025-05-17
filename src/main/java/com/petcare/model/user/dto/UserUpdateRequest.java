package com.petcare.model.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    private String name;
    private String lastName1;
    private String lastName2;
    private String phoneNumber;           // Cliente y Empleado
    private String profilePictureUrl;     // Solo Cliente
    private String address;               // Solo Cliente
}

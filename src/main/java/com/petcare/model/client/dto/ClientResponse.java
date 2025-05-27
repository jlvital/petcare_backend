package com.petcare.model.client.dto;

import com.petcare.model.user.dto.UserResponse;
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
    private double monthlySpending;
    private double totalSpending;
}
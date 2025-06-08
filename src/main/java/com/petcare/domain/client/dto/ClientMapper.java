package com.petcare.domain.client.dto;

import com.petcare.enums.Role;
import com.petcare.domain.client.Client;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ClientMapper {

    public static Client toEntity(ClientRequest request, PasswordEncoder encoder) {
        if (request == null || encoder == null) {
            return null;
        }

        Client client = new Client();
        client.setName(request.getName());
        client.setUsername(request.getUsername());
        client.setPassword(encoder.encode(request.getPassword()));
        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            client.setRecoveryEmail(request.getUsername());
        }
        client.setRole(Role.CLIENTE);

        return client;
    }

    public static ClientResponse toResponse(Client client) {
        if (client == null) {
            return null;
        }

        return ClientResponse.builder()
                .id(client.getId())
                .name(client.getName())
                .lastName1(client.getLastName1())
                .lastName2(client.getLastName2())
                .username(client.getUsername())
                .accountStatus(client.getAccountStatus())
                .role(client.getRole())
                .phone(client.getPhoneNumber())
                .profilePictureUrl(client.getProfilePictureUrl())
                .address(client.getAddress())
                .birthDate(client.getBirthDate())
                .gender(client.getGender())
                .genderLabel(client.getGender() != null ? client.getGender().getLabel() : null)
                .notificationStatus(client.getNotificationStatus())
                .notificationStatusLabel(client.getNotificationStatus() != null ? client.getNotificationStatus().getLabel() : null)
                .roleLabel(client.getRole() != null ? client.getRole().getLabel() : null)
                .accountStatusLabel(client.getAccountStatus() != null ? client.getAccountStatus().getLabel() : null)
                .monthlySpending(0) // se puede calcular luego
                .totalSpending(0)
                .build();
    }

    public static void updateEntityFromRequest(ClientUpdate request, Client client) {
        if (request == null || client == null) {
            return;
        }

        if (request.getName() != null) {
            client.setName(request.getName());
        }

        if (request.getLastName1() != null) {
            client.setLastName1(request.getLastName1());
        }

        if (request.getLastName2() != null) {
            client.setLastName2(request.getLastName2());
        }

        if (request.getRecoveryEmail() != null && !request.getRecoveryEmail().isBlank()) {
            client.setRecoveryEmail(request.getRecoveryEmail());
        }

        if (request.getPhoneNumber() != null) {
            client.setPhoneNumber(request.getPhoneNumber());
        }

        if (request.getAddress() != null) {
            client.setAddress(request.getAddress());
        }

        if (request.getGender() != null) {
            client.setGender(request.getGender());
        }

        if (request.getBirthDate() != null) {
            client.setBirthDate(request.getBirthDate());
        }

        if (request.getProfilePictureUrl() != null) {
            client.setProfilePictureUrl(request.getProfilePictureUrl());
        }

        if (request.getNotificationStatus() != null) {
            client.setNotificationStatus(request.getNotificationStatus());
        }
    }
}
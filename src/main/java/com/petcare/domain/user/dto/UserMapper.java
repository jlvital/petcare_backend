package com.petcare.domain.user.dto;

import com.petcare.domain.client.Client;
import com.petcare.domain.employee.Employee;
import com.petcare.domain.user.User;

public class UserMapper {

    public static UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName1(user.getLastName1())
                .lastName2(user.getLastName2())
                .username(user.getUsername())
                .accountStatus(user.getAccountStatus())
                .role(user.getRole())
                .roleLabel(user.getRole() != null ? user.getRole().getLabel() : null)
                .build();
    }

    public static void updateEntityFromRequest(UserUpdate request, User user) {
        if (request == null || user == null) {
            return;
        }

        if (request.getName() != null) {
            user.setName(request.getName());
        }

        if (request.getLastName1() != null) {
            user.setLastName1(request.getLastName1());
        }

        if (request.getLastName2() != null) {
            user.setLastName2(request.getLastName2());
        }

        // Campos específicos según el tipo de usuario
        if (user instanceof Client) {
            Client client = (Client) user;

            if (request.getPhoneNumber() != null) {
                client.setPhoneNumber(request.getPhoneNumber());
            }
            if (request.getProfilePictureUrl() != null) {
                client.setProfilePictureUrl(request.getProfilePictureUrl());
            }
            if (request.getAddress() != null) {
                client.setAddress(request.getAddress());
            }

        } else if (user instanceof Employee) {
            Employee employee = (Employee) user;

            if (request.getPhoneNumber() != null) {
                employee.setPhoneNumber(request.getPhoneNumber());
            }
        }
    }
}
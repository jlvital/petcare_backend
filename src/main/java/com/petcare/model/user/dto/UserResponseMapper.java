package com.petcare.model.user.dto;

import com.petcare.model.client.Client;
import com.petcare.model.client.dto.ClientResponse;
import com.petcare.model.employee.Employee;
import com.petcare.model.employee.dto.EmployeeResponse;
import com.petcare.model.user.User;

public class UserResponseMapper {

    public static ClientResponse toClientResponse(Client client) {
        if (client == null) return null;

        return ClientResponse.builder()
            .id(client.getId())
            .name(client.getName())
            .lastName1(client.getLastName1())
            .lastName2(client.getLastName2())
            .username(client.getUsername())
            .phone(client.getPhoneNumber())
            .profilePictureUrl(client.getProfilePictureUrl())
            .address(client.getAddress())
            .accountStatus(client.getAccountStatus())
            .role(client.getRole())
            .build();
    }

    public static EmployeeResponse toEmployeeResponse(Employee employee) {
        if (employee == null) return null;

        return EmployeeResponse.builder()
            .id(employee.getId())
            .name(employee.getName())
            .lastName1(employee.getLastName1())
            .lastName2(employee.getLastName2())
            .username(employee.getUsername())
            .startDate(employee.getStartDate())
            .accountStatus(employee.getAccountStatus())
            .role(employee.getRole())
            .build();
    }

    public static UserResponse toUserResponse(User User) {
        if (User == null) return null;

        return UserResponse.builder()
            .id(User.getId())
            .name(User.getName())
            .lastName1(User.getLastName1())
            .lastName2(User.getLastName2())
            .username(User.getUsername())
            .accountStatus(User.getAccountStatus())
            .role(User.getRole())
            .build();
    }
}
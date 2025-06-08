package com.petcare.domain.employee.dto;

import com.petcare.domain.employee.Employee;

public class EmployeeMapper {

    public static Employee toEntity(EmployeeRequest request) {
        if (request == null) {
            return null;
        }

        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setLastName1(request.getLastName1());
        employee.setLastName2(request.getLastName2());
        employee.setRecoveryEmail(request.getRecoveryEmail());
        employee.setStartDate(request.getStartDate());
        employee.setProfile(request.getProfile());

        return employee;
    }

    public static EmployeeResponse toResponse(Employee employee) {
        if (employee == null) {
            return null;
        }

        return EmployeeResponse.builder()
                .id(employee.getId())
                .name(employee.getName())
                .lastName1(employee.getLastName1())
                .lastName2(employee.getLastName2())
                .username(employee.getUsername())
                .accountStatus(employee.getAccountStatus())
                .accountStatusLabel(employee.getAccountStatus() != null ? employee.getAccountStatus().getLabel() : null)
                .role(employee.getRole())
                .roleLabel(employee.getRole() != null ? employee.getRole().getLabel() : null)
                .startDate(employee.getStartDate())
                .endDate(employee.getEndDate())
                .profile(employee.getProfile())
                .profileLabel(employee.getProfile() != null ? employee.getProfile().getLabel() : null)
                .gender(employee.getGender())
                .genderLabel(employee.getGender() != null ? employee.getGender().getLabel() : null)
                .build();
    }

    public static void updateEntityFromRequest(EmployeeUpdate request, Employee employee) {
        if (request == null || employee == null) {
            return;
        }

        if (request.getName() != null) {
            employee.setName(request.getName());
        }

        if (request.getLastName1() != null) {
            employee.setLastName1(request.getLastName1());
        }

        if (request.getLastName2() != null) {
            employee.setLastName2(request.getLastName2());
        }

        if (request.getRecoveryEmail() != null) {
            employee.setRecoveryEmail(request.getRecoveryEmail());
        }

        if (request.getPhoneNumber() != null) {
            employee.setPhoneNumber(request.getPhoneNumber());
        }

        if (request.getAddress() != null) {
            employee.setAddress(request.getAddress());
        }

        if (request.getGender() != null) {
            employee.setGender(request.getGender());
        }
    }
}
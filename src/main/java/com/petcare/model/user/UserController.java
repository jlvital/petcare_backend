package com.petcare.model.user;

import com.petcare.model.client.Client;
import com.petcare.model.employee.Employee;
import com.petcare.model.user.dto.UserUpdateRequest;
import com.petcare.model.user.dto.UserResponse;
import com.petcare.model.user.dto.UserResponseMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getMyProfile(@AuthenticationPrincipal User User) {
        UserResponse response;

        if (User instanceof Client) {
            response = UserResponseMapper.toClientResponse((Client) User);
        } else if (User instanceof Employee) {
            response = UserResponseMapper.toEmployeeResponse((Employee) User);
        } else {
            response = UserResponseMapper.toUserResponse(User);
        }

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> updateMyProfile(
            @AuthenticationPrincipal User User,
            @RequestBody UserUpdateRequest request) {

        if (request.getName() != null) User.setName(request.getName());
        if (request.getLastName1() != null) User.setLastName1(request.getLastName1());
        if (request.getLastName2() != null) User.setLastName2(request.getLastName2());

        if (User instanceof Client) {
            Client client = (Client) User;

            if (request.getPhoneNumber() != null) client.setPhoneNumber(request.getPhoneNumber());
            if (request.getProfilePictureUrl() != null) client.setProfilePictureUrl(request.getProfilePictureUrl());
            if (request.getAddress() != null) client.setAddress(request.getAddress());

            userService.saveForUserType(client);

        } else if (User instanceof Employee) {
            Employee employee = (Employee) User;

            if (request.getPhoneNumber() != null) employee.setPhoneNumber(request.getPhoneNumber());

            userService.saveForUserType(employee);
        } else {
            userService.saveForUserType(User);
        }

        log.info("Perfil actualizado correctamente para el usuario con ID: {}", User.getId());
        return ResponseEntity.ok("Perfil actualizado correctamente.");
    }
}
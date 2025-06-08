package com.petcare.domain.user.dto;

import com.petcare.enums.AccountStatus;
import com.petcare.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class UserResponse {

    private Long id;
    private String name;
    private String lastName1;
    private String lastName2;
    private String username;

    private AccountStatus accountStatus;
    private String accountStatusLabel;

    private Role role;
    private String roleLabel;
}
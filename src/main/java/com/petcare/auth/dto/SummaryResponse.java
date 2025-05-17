package com.petcare.auth.dto;

import com.petcare.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class SummaryResponse {
	
    private Long id;
    private String name;
    private String username;
    private Role role;
    private boolean active;
}
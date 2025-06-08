package com.petcare.auth;

import com.petcare.auth.dto.LoginRequest;
import com.petcare.auth.dto.LoginResponse;
import com.petcare.domain.client.dto.ClientRequest;

public interface AuthService {

	LoginResponse register(ClientRequest request);

	LoginResponse login(LoginRequest request);

	boolean changePassword(String username, String newPassword, String confirmPassword);

	void sendRecoveryLink(String email);

	boolean recoverAccount(String token, String newPassword, String confirmPassword);
	
	}
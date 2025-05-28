package com.petcare.auth;

import com.petcare.auth.dto.LoginRequest;
import com.petcare.auth.dto.AuthResponse;
import com.petcare.model.client.dto.ClientRegisterRequest;

public interface AuthService {

	AuthResponse register(ClientRegisterRequest request);

	AuthResponse login(LoginRequest request);

	boolean changeAuthUserPassword(String username, String newPassword);

	boolean resetPasswordWithToken(String token, String newPassword);

	void initiatePasswordRecovery(String email);

	void requestPasswordRecovery(String email);

	boolean reactivateAccount(String token);
}
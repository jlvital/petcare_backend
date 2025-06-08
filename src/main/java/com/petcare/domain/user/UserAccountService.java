package com.petcare.domain.user;

import java.util.List;
import java.util.Optional;

public interface UserAccountService {

	void activateAccount(Long id);

	void deactivateAccount(Long id);

	void deleteUserAccount(Long id);
	
	void deleteUserPermanently(Long id);

	List<User> findInactiveUsers(int days);

	Optional<User> findByRecoveryToken(String token);

	Optional<User> findByEmailForRecovery(String email);

	boolean updatePasswordWithToken(String token, String newPassword);
}
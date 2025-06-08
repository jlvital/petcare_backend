package com.petcare.domain.user;

import java.util.List;

import com.petcare.domain.user.dto.UserResponse;
import com.petcare.domain.user.dto.UserUpdate;
import org.springframework.data.domain.Page;

public interface UserService {
	
	List<User> listUsers();

	User getUserById(Long id);

	User getUserByUsername(String username);
	
	void saveForUserType(User User);
	
	void updateUserProfile(User user, UserUpdate request);
	
	UserResponse getProfile(User user);
	
	Page<UserResponse> findUsersWithFilters(int page, int size, String role, String status, String name);
}
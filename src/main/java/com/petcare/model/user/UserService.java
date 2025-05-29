package com.petcare.model.user;

import java.util.List;

public interface UserService {
	
	List<User> listUsers();

	User findUserById(Long id);

	User findUserByUsername(String username);

	void saveForUserType(User User);
}
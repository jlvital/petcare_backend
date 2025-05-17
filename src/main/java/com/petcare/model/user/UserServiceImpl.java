package com.petcare.model.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.petcare.enums.Role;
import com.petcare.exceptions.UserNotFoundException;
import com.petcare.model.client.Client;
import com.petcare.model.client.ClientRepository;
import com.petcare.model.employee.Employee;
import com.petcare.model.employee.EmployeeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    @Override
    public List<User> listUsers() {
        List<User> allUsers = new ArrayList<>();
        allUsers.addAll(clientRepository.findAll());
        allUsers.addAll(employeeRepository.findAll());
        return allUsers;
    }

    @Override
    public User findUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            log.warn("Usuario no encontrado con ID: {}", id);
            throw new UserNotFoundException("Usuario no encontrado con ID: " + id);
        }
        return userOptional.get();
    }

    @Override
    public User findUserByUsername(String email) {
        Optional<User> userOptional = userRepository.findByUsername(email);
        if (!userOptional.isPresent()) {
            log.warn("Usuario no encontrado con email: {}", email);
            throw new UserNotFoundException("Usuario no encontrado con email: " + email);
        }
        return userOptional.get();
    }

    @Override
    public void saveForUserType(User user) {
    	if (user instanceof Client) {
    		clientRepository.save((Client) user);
    	} else if (user instanceof Employee) {
    		employeeRepository.save((Employee) user);
    	} else if (user.getRole() == Role.ADMIN) {
            userRepository.save(user);
        } else {
        	log.warn("Intento de guardar usuario con rol no válido: {}", user.getRole());
            throw new IllegalArgumentException("Tipo de usuario no soportado para guardar");
        }
    }
}
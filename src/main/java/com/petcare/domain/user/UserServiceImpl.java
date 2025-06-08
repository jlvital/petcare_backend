package com.petcare.domain.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.petcare.domain.client.Client;
import com.petcare.domain.client.ClientRepository;
import com.petcare.domain.client.dto.ClientMapper;
import com.petcare.domain.employee.Employee;
import com.petcare.domain.employee.EmployeeRepository;
import com.petcare.domain.employee.dto.EmployeeMapper;
import com.petcare.domain.user.dto.UserMapper;
import com.petcare.domain.user.dto.UserResponse;
import com.petcare.domain.user.dto.UserUpdate;
import com.petcare.enums.AccountStatus;
import com.petcare.enums.Role;
import com.petcare.validators.AccountValidator;
import com.petcare.validators.UserValidator;

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
    public User getUserById(Long id) {
        return UserValidator.requireUserById(userRepository, id);
    }

    /**
     * Busca un usuario por su nombre de usuario (email corporativo o personal).
     * <p>
     * Este método utiliza una validación interna que lanza una excepción si el usuario no existe,
     * por lo que garantiza que el valor devuelto nunca será {@code null}.
     *
     * @param username nombre de usuario (email)
     * @return usuario correspondiente
     * @throws NotFoundException si no se encuentra ningún usuario con ese nombre
     */

    @Override
    public User getUserByUsername(String email) {
        User user = UserValidator.requireUserByUsername(userRepository, email);
        log.info("Usuario encontrado correctamente por email: {}", email);
        return user;
    }

    @Override
    public Page<UserResponse> findUsersWithFilters(int page, int size, String roleStr, String statusStr, String name) {
        Pageable pageable = PageRequest.of(page, size);

        Role role = null;
        AccountStatus status = null;

        try {
            if (roleStr != null && !roleStr.isBlank()) {
                role = Role.valueOf(roleStr.toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            log.warn("Rol desconocido recibido en filtros: {}", roleStr);
        }

        try {
            if (statusStr != null && !statusStr.isBlank()) {
                status = AccountStatus.valueOf(statusStr.toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            log.warn("Estado de cuenta desconocido recibido en filtros: {}", statusStr);
        }

        Page<User> result = userRepository.findAllWithFilters(role, status, name, pageable);
        return result.map(UserMapper::toResponse);
    }
    
    @Override
    public UserResponse getProfile(User user) {
        if (user instanceof Client) {
            return ClientMapper.toResponse((Client) user);
        } else if (user instanceof Employee) {
            return EmployeeMapper.toResponse((Employee) user);
        } else {
            return UserMapper.toResponse(user);
        }
    }
    
    
    
    @Override
    public void saveForUserType(User user) {
        Role role = user.getRole();

        if (user instanceof Client) {
            if (role != Role.CLIENTE) {
                String msg = "Se esperaba un usuario con rol 'Cliente', pero se recibió: " + role;
                log.warn("Inconsistencia al guardar cliente: {}", msg);
                throw new IllegalStateException(msg);
            }
            clientRepository.save((Client) user);
            log.info("Cliente con ID [{}] guardado correctamente", user.getId());
            return;
        }

        if (user instanceof Employee) {
            if (role != Role.EMPLEADO) {
                String msg = "Se esperaba un usuario con rol 'Empleado', pero se recibió: " + role;
                log.warn("Inconsistencia al guardar empleado: {}", msg);
                throw new IllegalStateException(msg);
            }
            employeeRepository.save((Employee) user);
            log.info("Empleado con ID [{}] guardado correctamente", user.getId());
            return;
        }

        if (role == Role.ADMIN) {
            userRepository.save(user);
            log.info("Administrador guardado correctamente: {}", user.getUsername());
            return;
        }

        log.warn("No se pudo guardar el usuario: tipo [{}], rol [{}] no compatibles.", 
                 user.getClass().getSimpleName(), role);
        throw new IllegalArgumentException("No se reconoce el tipo de usuario o el rol no es válido.");
    }
    
    @Override
    public void updateUserProfile(User user, UserUpdate request) {
    	AccountValidator.validateAccountIsActive(user);

        if (user == null || user.getId() == null) {
            log.warn("Intento de actualización con usuario nulo o sin ID.");
            throw new IllegalArgumentException("Usuario inválido para actualizar perfil.");
        }

        if (request == null) {
            log.warn("Datos de actualización nulos para el usuario ID {}", user.getId());
            throw new IllegalArgumentException("Los datos de actualización no pueden ser nulos.");
        }

        UserMapper.updateEntityFromRequest(request, user);
        saveForUserType(user);

        log.info("Perfil actualizado correctamente para el usuario con ID: {}", user.getId());
    }
}
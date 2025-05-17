package com.petcare.admin;

import com.petcare.enums.AccountStatus;
import com.petcare.enums.Role;
import com.petcare.exceptions.UserAlreadyExistsException;
import com.petcare.exceptions.UserNotFoundException;
import com.petcare.model.client.Client;
import com.petcare.model.client.ClientRepository;
import com.petcare.model.employee.Employee;
import com.petcare.model.employee.dto.EmployeeRegistrationRequest;
import com.petcare.model.employee.EmployeeRepository;
import com.petcare.model.employee.EmployeeService;
import com.petcare.model.user.User;
import com.petcare.model.user.UserRepository;
import com.petcare.model.user.UserService;
import com.petcare.admin.dto.AdminStatsResponse;
import com.petcare.email.EmailService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j

public class AdminServiceImpl implements AdminService {

    private final EmployeeRepository employeeRepository;
    private final ClientRepository clientRepository;
    private final EmployeeService employeeService;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final UserService userService;
    @Value("${system.admin.recovery.email}")
    private String adminRecoveryEmail;

    @Override
    public Employee registerNewEmployee(EmployeeRegistrationRequest request) {
        if (userRepository.existsByUsername(request.getRecoveryEmail())) {
            throw new UserAlreadyExistsException("Ya existe un usuario registrado con el correo: " + request.getRecoveryEmail());
        }

        Employee employee = new Employee();
        employee.setRecoveryEmail(request.getRecoveryEmail());
        employee.setName(request.getName());
        employee.setLastName1(request.getLastName1());
        employee.setLastName2(request.getLastName2());
        employee.setProfile(request.getProfile());
        employee.setStartDate(request.getStartDate());
        employee.setRole(Role.EMPLEADO);

        log.info("Delegando el registro del empleado a EmployeeService...");
        return employeeService.registerEmployee(employee);
    }

    @Override
    public void activateUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            log.warn("Usuario no encontrado con ID: {}", userId);
            throw new UserNotFoundException("Usuario no encontrado con ID: " + userId);
        }

        User User = userOptional.get();
        User.setAccountStatus(AccountStatus.ACTIVADA); 
        userService.saveForUserType(User);             
        log.info("Cuenta activada para el usuario con ID: {}", userId);
    }

    @Override
    public void deactivateUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            log.warn("Usuario no encontrado con ID: {}", userId);
            throw new UserNotFoundException("Usuario no encontrado con ID: " + userId);
        }

        User User = userOptional.get();
        User.setAccountStatus(AccountStatus.BLOQUEADA); 
        userService.saveForUserType(User);               
        log.info("Cuenta desactivada para el usuario con ID: {}", userId);
    }
    @Override
    public void deleteUser(Long userId) {
        Optional<Client> optionalClient = clientRepository.findById(userId);
        if (optionalClient.isPresent()) {
            clientRepository.deleteById(userId);
            log.info("Cliente eliminado con ID: {}", userId);
            return;
        }

        Optional<Employee> optionalEmployee = employeeRepository.findById(userId);
        if (optionalEmployee.isPresent()) {
            employeeRepository.deleteById(userId);
            log.info("Empleado eliminado con ID: {}", userId);
            return;
        }

        log.warn("No se encontró ningún usuario con ID: {}", userId);
        throw new UserNotFoundException("No se encontró ningún usuario con ID: " + userId);
    }

    @Override
    public List<Employee> listEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public List<Client> listClients() {
        return clientRepository.findAll();
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
    public Client findClientById(Long id) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (!optionalClient.isPresent()) {
            log.warn("Cliente no encontrado con ID: {}", id);
            throw new UserNotFoundException("Cliente no encontrado con ID: " + id);
        }
        return optionalClient.get();
    }

    @Override
    public Employee findEmployeeById(Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (!optionalEmployee.isPresent()) {
            log.warn("Empleado no encontrado con ID: {}", id);
            throw new UserNotFoundException("Empleado no encontrado con ID: " + id);
        }
        return optionalEmployee.get();
    }

    @Override
    public void recoverAdminPassword() {
        if (adminRecoveryEmail == null || adminRecoveryEmail.isBlank()) {
            log.error("El correo de recuperación del administrador no está configurado.");
            throw new IllegalStateException("El correo de recuperación no está configurado correctamente.");
        }

        try {
            String recoveryLink = "https://portal.petcare360.com/admin-recover-password";
            emailService.sendPasswordRecoveryEmail(adminRecoveryEmail, recoveryLink);
            log.info("Correo de recuperación de administrador enviado a: {}", adminRecoveryEmail);
        } catch (Exception e) {
            log.error("Error enviando correo de recuperación al administrador: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo enviar el correo de recuperación de contraseña al administrador", e);
        }
    }

    @Override
    public void updateProductPrice(Long productId, double newPrice) {
        log.info("Precio actualizado para el producto con ID: {} a nuevo precio: {}", productId, newPrice);
    }

    @Override
    public void updateStock(Long productId, int quantity) {
        log.info("Stock actualizado para el producto con ID: {} a nueva cantidad: {}", productId, quantity);
    }

    @Override
    public AdminStatsResponse getDashboardStats() {
        return null;
    }
}
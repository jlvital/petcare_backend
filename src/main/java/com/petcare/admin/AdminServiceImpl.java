package com.petcare.admin;

import com.petcare.admin.dto.AdminBookingStats;
import com.petcare.admin.dto.AdminServiceStats;
import com.petcare.domain.booking.Booking;
import com.petcare.domain.booking.BookingRepository;
import com.petcare.domain.client.Client;
import com.petcare.domain.client.ClientRepository;
import com.petcare.domain.employee.Employee;
import com.petcare.domain.employee.EmployeeRepository;
import com.petcare.domain.employee.EmployeeService;
import com.petcare.domain.employee.dto.EmployeeRequest;
import com.petcare.domain.product.ProductService;
import com.petcare.domain.user.User;
import com.petcare.domain.user.UserRepository;
import com.petcare.domain.user.UserService;
import com.petcare.domain.user.dto.UserMapper;
import com.petcare.domain.user.dto.UserUpdate;
import com.petcare.enums.AccountStatus;
import com.petcare.enums.BookingStatus;
import com.petcare.enums.BookingType;
import com.petcare.enums.Role;
import com.petcare.exceptions.*;
import com.petcare.validators.ProductValidator;
import com.petcare.validators.UserValidator;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final EmployeeRepository employeeRepository;
    private final ClientRepository clientRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final EmployeeService employeeService;
    private final ProductService productService;

	// ╔════════════════════════════════════════════════════╗
	// ║ GESTIÓN DE USUARIOS								║
	// ╚════════════════════════════════════════════════════╝
    
    @Override
    public void activateUser(Long userId) {
        User user = UserValidator.requireUserById(userRepository, userId);
        user.setAccountStatus(AccountStatus.ACTIVA);
        userService.saveForUserType(user);
        log.info("Cuenta activada para el usuario con ID: {}", userId);
    }

    @Override
    public void deactivateUser(Long userId) {
        User user = UserValidator.requireUserById(userRepository, userId);
        user.setAccountStatus(AccountStatus.BLOQUEADA);
        userService.saveForUserType(user);
        log.info("Cuenta bloqueada para el usuario con ID: {}", userId);
    }

    @Override
    public void deleteUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            log.warn("No se encontró ningún usuario con id {}", userId);
            throw new NotFoundException("Usuario no encontrado con ID " + userId);
        }

        User user = optionalUser.get();
        Role role = user.getRole();

        if (role == Role.CLIENTE) {
            clientRepository.deleteById(userId);
            log.info("Cliente con id = {} eliminado con éxito.", userId);
        } else if (role == Role.EMPLEADO) {
            employeeRepository.deleteById(userId);
            log.info("Empleado con id = {} eliminado correctamente.", userId);
        } else {
            userRepository.deleteById(userId);
            log.info("Usuario con rol {} e id = {} eliminado del sistema.", role.name(), userId);
        }
    }

    @Override
    public User findUserById(Long id) {
        return UserValidator.requireUserById(userRepository, id);
    }

    @Override
    public void updateUser(Long userId, UserUpdate request) {
        User user = UserValidator.requireUserById(userRepository, userId);
        UserMapper.updateEntityFromRequest(request, user);
        userService.saveForUserType(user);
        log.info("Usuario actualizado correctamente. ID: {}", userId);
    }
    
    @Override
    public List<User> getAllUsers() {
    	return userRepository.findAll();
    }

    // ╔════════════════════════════════════════════════════╗
 	// ║ GESTIÓN DE EMPLEADOS								║
 	// ╚════════════════════════════════════════════════════╝

    @Override
    public Employee registerEmployee(EmployeeRequest request) {
        return employeeService.registerEmployee(request);
    }

    @Override
    public List<Employee> listEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee findEmployeeById(Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            return optionalEmployee.get();
        }

        log.warn("Empleado no encontrado con ID: {}", id);
        throw new NotFoundException("Empleado no encontrado con ID: " + id);
    }

    // ╔════════════════════════════════════════════════════╗
 	// ║ GESTIÓN DE CLIENTES								║
 	// ╚════════════════════════════════════════════════════╝

    @Override
    public List<Client> listClients() {
        return clientRepository.findAll();
    }

    @Override
    public Client findClientById(Long id) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isPresent()) {
            return optionalClient.get();
        }

        log.warn("Cliente no encontrado con ID: {}", id);
        throw new NotFoundException("Cliente no encontrado con ID: " + id);
    }

    // ╔════════════════════════════════════════════════════╗
 	// ║ GESTIÓN DE PRODUCTOS								║
 	// ╚════════════════════════════════════════════════════╝

    @Override
    public void updateProductPrice(Long productId, double newPrice) {
        ProductValidator.validatePrice(newPrice);
        log.info("Validación superada para precio. Producto ID: {}, nuevo precio: {}", productId, newPrice);
        productService.updatePrice(productId, newPrice);
    }

    @Override
    public void updateStock(Long productId, int quantity) {
        ProductValidator.validateStock(quantity);
        log.info("Validación superada para stock. Producto ID: {}, nueva cantidad: {}", productId, quantity);
        productService.updateStock(productId, quantity);
    }

    // ╔════════════════════════════════════════════════════╗
 	// ║ GESTIÓN DE ESTADÍSTICAS							║
 	// ╚════════════════════════════════════════════════════╝
     
    @Override
    public AdminBookingStats getBookingStats() {
        List<Booking> allBookings = bookingRepository.findAll();

        int totalBookings = allBookings.size();
        int confirmed = 0;
        int cancelled = 0;
        int aborted = 0;
        int completed = 0;

        Map<BookingType, Integer> bookingsByType = new HashMap<>();
        Map<BookingStatus, Integer> bookingsByStatus = new HashMap<>();

        for (Booking booking : allBookings) {
            BookingStatus status = booking.getStatus();
            BookingType type = booking.getType();

            if (status == BookingStatus.CONFIRMADA) confirmed++;
            if (status == BookingStatus.CANCELADA) cancelled++;
            if (status == BookingStatus.ANULADA) aborted++;
            if (status == BookingStatus.COMPLETADA) completed++;

            if (bookingsByStatus.containsKey(status)) {
                bookingsByStatus.put(status, bookingsByStatus.get(status) + 1);
            } else {
                bookingsByStatus.put(status, 1);
            }

            if (bookingsByType.containsKey(type)) {
                bookingsByType.put(type, bookingsByType.get(type) + 1);
            } else {
                bookingsByType.put(type, 1);
            }
        }

        AdminBookingStats stats = new AdminBookingStats();
        stats.setTotalBookings(totalBookings);
        stats.setConfirmedBookings(confirmed);
        stats.setCancelledBookings(cancelled);
        stats.setAbortedBookings(aborted);
        stats.setCompletedBookings(completed);
        stats.setTotalClients(clientRepository.findAll().size());
        stats.setTotalEmployees(employeeRepository.findAll().size());
        stats.setBookingsByType(bookingsByType);
        stats.setBookingsByStatus(bookingsByStatus);

        log.info("Estadísticas de citas generadas correctamente.");
        return stats;
    }

    @Override
    public AdminServiceStats getServiceStats() {
        List<Booking> allBookings = bookingRepository.findAll();

        Map<BookingType, Integer> totalByService = new EnumMap<>(BookingType.class);
        int totalBookings = allBookings.size();

        for (Booking booking : allBookings) {
            BookingType type = booking.getType();

            if (totalByService.containsKey(type)) {
                totalByService.put(type, totalByService.get(type) + 1);
            } else {
                totalByService.put(type, 1);
            }
        }

        BookingType mostDemanded = null;
        BookingType leastDemanded = null;
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;

        for (BookingType type : totalByService.keySet()) {
            int count = totalByService.get(type);
            if (count > max) {
                max = count;
                mostDemanded = type;
            }
            if (count < min) {
                min = count;
                leastDemanded = type;
            }
        }

        Map<BookingType, Double> percentageByService = new EnumMap<>(BookingType.class);
        for (BookingType type : totalByService.keySet()) {
            int count = totalByService.get(type);

            double percentage = 0.0;
            if (totalBookings > 0) {
                percentage = (count * 100.0) / totalBookings;
            }

            double rounded = Math.round(percentage * 100.0) / 100.0;
            percentageByService.put(type, rounded);
        }

        AdminServiceStats response = new AdminServiceStats();
        response.setTotalBookings(totalBookings);
        response.setMostDemandedService(mostDemanded);
        response.setLeastDemandedService(leastDemanded);
        response.setTotalByService(totalByService);
        response.setPercentageByService(percentageByService);

        log.info("Estadísticas por servicio generadas correctamente.");
        return response;
    }
    
    @Override
    public Page<Booking> getBookingsFromLastDays(Integer days, Pageable pageable) {
        log.info("Recuperando citas: últimos {} días con paginación {}", days, pageable);

        if (days == null || days <= 0) {
            return bookingRepository.findAll(pageable);
        }

        LocalDate cutoffDate = LocalDate.now().minusDays(days);
        return bookingRepository.findByDateAfter(cutoffDate, pageable);
    }
}
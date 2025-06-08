package com.petcare.admin;

import java.util.List;


import com.petcare.admin.dto.AdminBookingStats;
import com.petcare.admin.dto.AdminServiceStats;
import com.petcare.domain.booking.Booking;
import com.petcare.domain.client.Client;
import com.petcare.domain.employee.Employee;
import com.petcare.domain.employee.dto.EmployeeRequest;
import com.petcare.domain.user.User;
import com.petcare.domain.user.dto.UserUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
/**
 * Interfaz que define todos los servicios disponibles para el panel del administrador.
 * Estas operaciones solo pueden ser invocadas por un usuario con rol ADMIN.
 */

public interface AdminService {

	// ╔════════════════════════════════════════════════════╗
	// ║ GESTIÓN DE USUARIOS								║
	// ╚════════════════════════════════════════════════════╝

    void activateUser(Long userId);
    void deactivateUser(Long userId);
    void deleteUser(Long userId);
    User findUserById(Long id);
    void updateUser(Long userId, UserUpdate request);

	// ╔════════════════════════════════════════════════════╗
	// ║ GESTIÓN DE EMPLEADOS								║
	// ╚════════════════════════════════════════════════════╝

    Employee registerEmployee(EmployeeRequest request);
    List<Employee> listEmployees();
    Employee findEmployeeById(Long id);

	// ╔════════════════════════════════════════════════════╗
	// ║ GESTIÓN DE CLIENTES								║
	// ╚════════════════════════════════════════════════════╝

    List<Client> listClients();
    Client findClientById(Long id);

	// ╔════════════════════════════════════════════════════╗
	// ║ GESTIÓN DE PRODUCTOS								║
	// ╚════════════════════════════════════════════════════╝

    void updateProductPrice(Long productId, double newPrice);
    void updateStock(Long productId, int quantity);

	// ╔════════════════════════════════════════════════════╗
	// ║ GESTIÓN DE ESTADÍSTICAS							║
	// ╚════════════════════════════════════════════════════╝

    AdminBookingStats getBookingStats();
    AdminServiceStats getServiceStats();
    Page<Booking> getBookingsFromLastDays(Integer days, Pageable pageable);
	List<User> getAllUsers();
}
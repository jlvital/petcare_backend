package com.petcare.admin;

import java.util.List;

import com.petcare.admin.dto.BookingStatsResponse;
import com.petcare.model.client.Client;
import com.petcare.model.employee.Employee;
import com.petcare.model.employee.dto.EmployeeRegisterRequest;
import com.petcare.model.user.User;

public interface AdminService {

	void activateUser(Long userId);

	void deactivateUser(Long userId);

	void deleteUser(Long userId);

	User findUserById(Long id);

	List<Client> listClients();

	Client findClientById(Long id);

	Employee registerNewEmployee(EmployeeRegisterRequest request);

	List<Employee> listEmployees();

	Employee findEmployeeById(Long id);

	void recoverAdminPassword();

	void updateProductPrice(Long productId, double newPrice);

	void updateStock(Long productId, int quantity);

	BookingStatsResponse getBookingStats();
}
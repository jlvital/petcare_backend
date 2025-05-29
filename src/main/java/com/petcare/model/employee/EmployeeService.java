package com.petcare.model.employee;

import com.petcare.model.employee.dto.EmployeeUpdateRequest;

public interface EmployeeService {

	Employee findEmployeeById(Long id);

	/**
	 * Busca un empleado usando su correo corporativo (username). Este correo es
	 * generado automáticamente y se usa como identificador de login.
	 *
	 * @param username el correo corporativo del empleado
	 * @return el empleado correspondiente
	 */
	Employee findEmployeeByUsername(String username);

	Employee registerEmployee(Employee employee);

	void updateEmployeeProfile(Employee employee, EmployeeUpdateRequest request);
}
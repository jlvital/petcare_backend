package com.petcare.domain.employee;

import com.petcare.domain.employee.dto.EmployeeRequest;
import com.petcare.domain.employee.dto.EmployeeUpdate;

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

	Employee registerEmployee(EmployeeRequest request);

	void updateEmployeeProfile(Employee employee, EmployeeUpdate request);
}
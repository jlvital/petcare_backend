package com.petcare.model.employee;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petcare.model.employee.dto.EmployeeUpdateRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

	private final EmployeeService employeeService;
	
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('EMPLEADO')")
    public ResponseEntity<String> getDashboard() {
        log.info("Acceso concedido al panel del empleado");
        return ResponseEntity.ok("Bienvenido al panel del empleado");
    }
    
    @PutMapping("/profile")
    @PreAuthorize("hasRole('EMPLEADO')")
    public ResponseEntity<String> updateEmployeeProfile(@RequestBody EmployeeUpdateRequest request,
                                                        @AuthenticationPrincipal Employee employee) {
        if (employee == null || employee.getId() == null) {
            log.warn("Acceso inválido: empleado no autenticado.");
            return ResponseEntity.status(403).body("No autorizado");
        }

        employeeService.updateEmployeeProfile(employee, request);
        log.info("Empleado ID {} actualizó su perfil correctamente", employee.getId());
        return ResponseEntity.ok("Perfil de empleado actualizado correctamente.");
    }
}
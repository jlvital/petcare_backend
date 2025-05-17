package com.petcare.model.employee;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('EMPLEADO')")
    public ResponseEntity<String> getDashboard() {
        log.info("Acceso concedido al panel del empleado");
        return ResponseEntity.ok("Bienvenido al panel del empleado");
    }
}

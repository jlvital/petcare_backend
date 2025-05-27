package com.petcare.admin;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.petcare.admin.dto.BookingStatsResponse;
import com.petcare.config.SystemAdmin;
import com.petcare.model.employee.Employee;
import com.petcare.model.employee.dto.EmployeeRegistrationRequest;
import com.petcare.model.employee.dto.EmployeeResponse;
import com.petcare.model.user.dto.UserResponseMapper;
import com.petcare.utils.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;
    private final SystemAdmin systemAdmin;

    // ╔══════════════════════════════════════════════════════════════╗
    // ║                    ACCESO AL DASHBOARD ADMIN                ║
    // ╚══════════════════════════════════════════════════════════════╝
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getDashboard() {
        log.info("Acceso concedido al panel del administrador.");
        return ResponseEntity.ok("Bienvenido al panel del administrador");
    }

    // ╔══════════════════════════════════════════════════════════════╗
    // ║                RECUPERACIÓN DE CONTRASEÑA ADMIN             ║
    // ╚══════════════════════════════════════════════════════════════╝
    @PostMapping("/recover-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> recoverPassword(@RequestParam String email, HttpServletRequest request) {
        log.info("Solicitud de recuperación de contraseña para correo: {}", email);

        if (!systemAdmin.isAdmin(email)) {
            log.warn("❌ Acceso denegado. El correo proporcionado no es válido como administrador: {}", email);
            return buildErrorResponse("Correo inválido.", HttpStatus.BAD_REQUEST, request);
        }

        adminService.recoverAdminPassword();
        log.info("Correo de recuperación enviado con éxito a {}", email);
        return ResponseEntity.ok("Se ha enviado un correo con instrucciones de recuperación.");
    }

    // ╔══════════════════════════════════════════════════════════════╗
    // ║                REGISTRO DE NUEVO EMPLEADO                   ║
    // ╚══════════════════════════════════════════════════════════════╝
    @PostMapping("/register-employee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponse> registerEmployee(@RequestBody EmployeeRegistrationRequest request) {
        log.info("Registro solicitado para nuevo empleado: {} {} <{}>",
            request.getName(),
            request.getLastName1(),
            request.getRecoveryEmail()
        );

        Employee empleado = adminService.registerNewEmployee(request);
        EmployeeResponse response = UserResponseMapper.toEmployeeResponse(empleado);

        log.info("Empleado registrado: ID={}, usuario corporativo={}",
            empleado.getId(),
            empleado.getUsername()
        );

        return ResponseEntity.ok(response);
    }

    // ╔══════════════════════════════════════════════════════════════╗
    // ║                 ESTADÍSTICAS DEL PANEL ADMIN                 ║
    // ╚══════════════════════════════════════════════════════════════╝
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookingStatsResponse> getStats() {
        log.info("Solicitando estadísticas del panel de administración...");
        BookingStatsResponse stats = adminService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    // ╔══════════════════════════════════════════════════════════════╗
    // ║                  UTILIDAD: CONSTRUCCIÓN DE ERRORES           ║
    // ╚══════════════════════════════════════════════════════════════╝
    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(status.value());
        error.setError(status.getReasonPhrase());
        error.setMessage(message);
        error.setPath(request.getRequestURI());
        error.setErrorCode("ERR-" + status.value());
        return new ResponseEntity<>(error, status);
    }
}
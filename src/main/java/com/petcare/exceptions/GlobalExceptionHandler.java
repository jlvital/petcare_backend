package com.petcare.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.petcare.utils.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // ╔════════════════════════════════════════════════════╗
    // ║                EXCEPCIONES DE USUARIO              ║
    // ╚════════════════════════════════════════════════════╝

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request, "USER_NOT_FOUND");
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT, request, "USER_ALREADY_EXISTS");
    }

    // ╔════════════════════════════════════════════════════╗
    // ║           EXCEPCIONES DE AUTENTICACIÓN             ║
    // ╚════════════════════════════════════════════════════╝

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ErrorResponse> handleIncorrectPassword(IncorrectPasswordException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED, request, "INCORRECT_PASSWORD");
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ErrorResponse> handlePasswordMismatch(PasswordMismatchException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request, "PASSWORD_MISMATCH");
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidToken(InvalidTokenException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED, request, "INVALID_TOKEN");
    }

    // ╔════════════════════════════════════════════════════╗
    // ║         EXCEPCIONES FUNCIONALES DEL SISTEMA        ║
    // ╚════════════════════════════════════════════════════╝

    @ExceptionHandler(BookingException.class)
    public ResponseEntity<ErrorResponse> handleBookingException(BookingException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request, "BOOKING_ERROR");
    }

    @ExceptionHandler(AdminRecoveryEmailException.class)
    public ResponseEntity<ErrorResponse> handleAdminRecoveryEmail(AdminRecoveryEmailException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request, "ADMIN_RECOVERY_EMAIL_NOT_CONFIGURED");
    }

    // ╔════════════════════════════════════════════════════╗
    // ║       ERRORES GENERALES Y NO CONTROLADOS           ║
    // ╚════════════════════════════════════════════════════╝

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("Parámetros no válidos en la solicitud: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request, "ILLEGAL_ARGUMENT");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex, HttpServletRequest request) {
        log.error("Excepción inesperada: {}", ex.getMessage(), ex);
        return buildErrorResponse("Ha ocurrido un error inesperado.", HttpStatus.INTERNAL_SERVER_ERROR, request, "INTERNAL_SERVER_ERROR");
    }

    // ╔════════════════════════════════════════════════════╗
    // ║     MÉTODO INTERNO PARA CONSTRUIR LA RESPUESTA     ║
    // ╚════════════════════════════════════════════════════╝

    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status, HttpServletRequest request, String errorCode) {
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(status.value());
        error.setError(status.getReasonPhrase());
        error.setMessage(message);
        error.setPath(request.getRequestURI());
        error.setErrorCode(errorCode);
        return new ResponseEntity<>(error, status);
    }
}
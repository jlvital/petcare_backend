package com.petcare.exceptions;

import com.petcare.utils.constants.MessageConstants;
import com.petcare.utils.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // ╔════════════════════════════════════════════════════╗
    // ║ EXCEPCIONES PERSONALIZADAS DEL SISTEMA            ║
    // ╚════════════════════════════════════════════════════╝

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request, "NOT_FOUND");
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExists(AlreadyExistsException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT, request, "ALREADY_EXISTS");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthentication(AuthenticationException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED, request, "AUTHENTICATION_ERROR");
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleAuthorization(UnauthorizedException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN, request, "AUTHORIZATION_ERROR");
    }

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<ErrorResponse> handleAccount(AccountException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN, request, "ACCOUNT_ERROR");
    }

    @ExceptionHandler(BookingException.class)
    public ResponseEntity<ErrorResponse> handleBooking(BookingException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request, "BOOKING_ERROR");
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request, "BUSINESS_ERROR");
    }

    @ExceptionHandler(ConfigurationException.class)
    public ResponseEntity<ErrorResponse> handleConfiguration(ConfigurationException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request, "CONFIGURATION_ERROR");
    }

    @ExceptionHandler(PasswordException.class)
    public ResponseEntity<ErrorResponse> handlePassword(PasswordException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request, "PASSWORD_ERROR");
    }

    @ExceptionHandler(DataException.class)
    public ResponseEntity<ErrorResponse> handleData(DataException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request, "DATA_ERROR");
    }

    // ╔════════════════════════════════════════════════════╗
    // ║ ERRORES DE VALIDACIÓN EN FORMULARIOS              ║
    // ╚════════════════════════════════════════════════════╝

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            org.springframework.web.bind.MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<String> errorList = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorList.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
        }

        log.warn("Errores de validación detectados: {}", errorList);
        return buildErrorResponse(MessageConstants.VALIDATION_ERROR, HttpStatus.BAD_REQUEST, request,
                "VALIDATION_ERROR", errorList);
    }

    // ╔════════════════════════════════════════════════════╗
    // ║ ERRORES GENÉRICOS Y NO CONTROLADOS                ║
    // ╚════════════════════════════════════════════════════╝

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex,
                                                               HttpServletRequest request) {
        log.warn("Parámetros no válidos: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request, "ILLEGAL_ARGUMENT");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex, HttpServletRequest request) {
        log.error("Error inesperado: {}", ex.getMessage(), ex);
        return buildErrorResponse(MessageConstants.UNEXPECTED_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, request,
                "INTERNAL_SERVER_ERROR");
    }

    // ╔════════════════════════════════════════════════════╗
    // ║ RESPUESTAS DE ERROR                                ║
    // ╚════════════════════════════════════════════════════╝

    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status,
                                                             HttpServletRequest request, String errorCode,
                                                             List<String> errors) {
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(status.value());
        error.setError(status.getReasonPhrase());
        error.setMessage(message);
        error.setPath(request.getRequestURI());
        error.setErrorCode(errorCode);
        error.setErrors(errors);

        return new ResponseEntity<>(error, status);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status,
                                                             HttpServletRequest request, String errorCode) {
        return buildErrorResponse(message, status, request, errorCode, null);
    }
}
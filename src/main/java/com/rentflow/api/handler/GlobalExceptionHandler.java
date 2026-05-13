package com.rentflow.api.handler;

import com.rentflow.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(ValidationException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation Error", ex.getMessage());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateResourceException(DuplicateResourceException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, "Duplicate Resource", ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Resource Not Found", ex.getMessage());
    }

    @ExceptionHandler(VehicleNotAvailableException.class)
    public ResponseEntity<Map<String, Object>> handleVehicleNotAvailableException(VehicleNotAvailableException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, "Vehicle Not Available", ex.getMessage());
    }

    @ExceptionHandler(InvalidVehicleStateException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidVehicleStateException(InvalidVehicleStateException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, "Invalid Vehicle State", ex.getMessage());
    }

    @ExceptionHandler(InvalidRentalDateException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidRentalDateException(InvalidRentalDateException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Rental Date", ex.getMessage());
    }

    @ExceptionHandler(RentFlowException.class)
    public ResponseEntity<Map<String, Object>> handleRentFlowException(RentFlowException ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "RentFlow Error", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected Error", ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(
            HttpStatus status,
            String error,
            String message
    ) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", error);
        response.put("message", message);

        return new ResponseEntity<>(response, status);
    }
}

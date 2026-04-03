package com.example.bankcards.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;


/**
 * Глобальный обработчик исключений для REST API.
 */
@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Map<String,String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String filedName = ((FieldError) error).getField();
            String errorMsg = error.getDefaultMessage();
            errors.put(filedName, errorMsg);
        });
        log.warn("Request validation failed: {}", errors);
        return ResponseEntity.badRequest().body(errors);
    }

    // TODO add entity not found exception

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Map<String,String>> handleException(Exception ex) {
        Map<String,String> errors = new HashMap<>();
        errors.put("message", "Internal Server Error");
        log.error(ex.getMessage(), ex);
        return ResponseEntity.internalServerError().body(errors);
    }
}

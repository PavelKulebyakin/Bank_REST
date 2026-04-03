package com.example.bankcards.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.HashMap;
import java.util.Map;


/**
 * Глобальный обработчик исключений для REST API.
 */
@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Map<String,String>> handleDTOValidationException(MethodArgumentNotValidException ex) {
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String filedName = ((FieldError) error).getField();
            String errorMsg = error.getDefaultMessage();
            errors.put(filedName, errorMsg);
        });
        log.warn("Request validation failed: {}", errors);
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<Map<String, String>> handleRequestVariableValidationException(HandlerMethodValidationException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getParameterValidationResults().forEach(paramResult ->
            paramResult.getResolvableErrors().forEach(error -> {
                String paramName = paramResult.getMethodParameter().getParameterName();
                String errorMsg = error.getDefaultMessage();
                errors.put(paramName, errorMsg);
            })
        );

        log.warn("Handler method validation failed: {}", errors);
        return ResponseEntity.badRequest().body(errors);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<Map<String, String>> handleJsonParseException(HttpMessageNotReadableException ex) {
        log.warn(ex.getMessage(), ex);
        Map<String, String> error = new HashMap<>();
        error.put("message", "Malformed JSON request");
        return ResponseEntity.badRequest().body(error);
    }

    // TODO add entity not found exception

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Map<String,String>> handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        Map<String,String> errors = new HashMap<>();
        errors.put("message", "Internal Server Error");
        return ResponseEntity.internalServerError().body(errors);
    }
}

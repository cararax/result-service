package com.carara.result.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@Log4j2(topic = "ResultGlobalExceptionHandler")
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).toList();
        log.error("MethodArgumentNotValidException: {}", errors);
        return new ResponseEntity<>(getErrorsMap(errors, BAD_REQUEST.value()), new HttpHeaders(), BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        String error = ex.getCause().getMessage();
        List<String> errors = List.of("Database error", error);
        log.error("DataIntegrityViolationException: {}", error);
        return new ResponseEntity<>(getErrorsMap(errors, INTERNAL_SERVER_ERROR.value()), new HttpHeaders(), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, List<String>>> handleNotFoundException(EntityNotFoundException ex) {
        log.error("EntityNotFoundException: " + ex.getMessage());
        return new ResponseEntity<>(getErrorsMap(List.of(ex.getMessage()), NOT_FOUND.value()), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<Map<String, List<String>>> handleRuntimeExceptions(RuntimeException ex) {
        log.error("RuntimeException: " + ex.getMessage());
        return new ResponseEntity<>(getErrorsMap(List.of(ex.getMessage()), INTERNAL_SERVER_ERROR.value()), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Map<String, List<String>>> handleGeneralExceptions(Exception ex) {
        if (ex instanceof RuntimeException) {
            log.error("RuntimeException: " + ex.getMessage());

        } else {
            log.error("Exception: " + ex.getMessage());
        }
        return new ResponseEntity<>(getErrorsMap(List.of(ex.getMessage()), INTERNAL_SERVER_ERROR.value()), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors, Integer status) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        errorResponse.put("timestamp", List.of(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        errorResponse.put("status", List.of(status.toString()));
        return errorResponse;
    }


}
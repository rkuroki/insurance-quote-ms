package com.insurance.insurancequote.controller;

import com.insurance.insurancequote.dto.SimpleErrorResponseDTO;
import com.insurance.insurancequote.exception.InsuranceQuoteRuleValidationException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static com.insurance.insurancequote.dto.SimpleErrorResponseDTO.create;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionResponseHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<SimpleErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex) {
        SimpleErrorResponseDTO errorResponse = create(BAD_REQUEST.name(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<SimpleErrorResponseDTO> handleJakartaValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        var bindingResult = ex.getBindingResult();
        if (bindingResult != null) {
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        }
        SimpleErrorResponseDTO errorResponse = create(BAD_REQUEST.name(), "Invalid payload, check 'details'.", errors);
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(InsuranceQuoteRuleValidationException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ResponseEntity<SimpleErrorResponseDTO> handleInsuranceQuoteRuleValidationException(InsuranceQuoteRuleValidationException ex) {
        SimpleErrorResponseDTO errorResponse = create(UNPROCESSABLE_ENTITY.name(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<SimpleErrorResponseDTO> handleEntityNotFoundException(EntityNotFoundException ex) {
        SimpleErrorResponseDTO errorResponse = create(NOT_FOUND.name(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }

}

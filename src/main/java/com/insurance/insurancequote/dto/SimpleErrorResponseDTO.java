package com.insurance.insurancequote.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/*
 * Very simple error response based on 'org.springframework.web.ErrorResponse'.
 */
@Getter
@AllArgsConstructor
public class SimpleErrorResponseDTO {

    private final String statusCode;
    private final String message;
    private final Map<String, String> details;

    public static SimpleErrorResponseDTO create(String statusCode, String message, Map<String, String> details) {
        return new SimpleErrorResponseDTO(statusCode, message, details);
    }

    public static SimpleErrorResponseDTO create(String statusCode, String message) {
        return new SimpleErrorResponseDTO(statusCode, message, null);
    }

}

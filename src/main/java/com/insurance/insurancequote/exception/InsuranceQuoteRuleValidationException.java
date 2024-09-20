package com.insurance.insurancequote.exception;

public class InsuranceQuoteRuleValidationException extends RuntimeException {

    public InsuranceQuoteRuleValidationException(String message) {
        super(message);
    }

    public InsuranceQuoteRuleValidationException(String message, Throwable cause) {
        super(message, cause);
    }

}

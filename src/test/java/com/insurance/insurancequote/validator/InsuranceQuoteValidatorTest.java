package com.insurance.insurancequote.validator;

import com.insurance.insurancequote.dto.InsuranceQuoteDTO;
import com.insurance.insurancequote.exception.InsuranceQuoteRuleValidationException;
import com.insurance.insurancequote.external.dto.MonthlyPremiumAmountDTO;
import com.insurance.insurancequote.external.dto.OfferDTO;
import com.insurance.insurancequote.external.dto.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class InsuranceQuoteValidatorTest {

    private InsuranceQuoteValidator validator;

    @BeforeEach
    void setUp() {
        validator = new RuleCompositeValidator();
    }

    @Test
    void testValidateInsuranceQuote_Success() {
        var quote = createValidQuote();
        var product = createActiveProduct();
        var offer = createActiveOffer();

        assertDoesNotThrow(() -> validator.validateInsuranceQuote(quote, product, offer));
    }

    @Test
    void testValidateInsuranceQuote_Failure_InactiveProduct() {
        var quote = createValidQuote();
        var product = createInactiveProduct();
        var offer = createActiveOffer();

        InsuranceQuoteRuleValidationException exception = assertThrows(InsuranceQuoteRuleValidationException.class, () -> {
            validator.validateInsuranceQuote(quote, product, offer);
        });
        assertEquals("Product is not active.", exception.getMessage());
    }

    @Test
    void testValidateInsuranceQuote_Failure_InactiveOffer() {
        var quote = createValidQuote();
        var product = createActiveProduct();
        var offer = createInactiveOffer();

        InsuranceQuoteRuleValidationException exception = assertThrows(InsuranceQuoteRuleValidationException.class, () -> {
            validator.validateInsuranceQuote(quote, product, offer);
        });
        assertEquals("Offer is not active.", exception.getMessage());
    }

    @Test
    void testValidateInsuranceQuote_Failure_ExceedingCoverage() {
        var quote = createValidQuote();
        var product = createActiveProduct();
        var offer = createActiveOffer();

        // Exceeding coverage amount for "Incêndio"
        quote.getCoverages().put("Incêndio", BigDecimal.valueOf(600000.0));

        InsuranceQuoteRuleValidationException exception = assertThrows(InsuranceQuoteRuleValidationException.class, () -> {
            validator.validateInsuranceQuote(quote, product, offer);
        });
        assertEquals("The coverage value for [Incêndio] exceeds the maximum allowed.", exception.getMessage());
    }

    private InsuranceQuoteDTO createValidQuote() {
        var quote = new InsuranceQuoteDTO();
        quote.setProductId("1b2da7cc-b367-4196-8a78-9cfeec21f587");
        quote.setOfferId("adc56d77-348c-4bf0-908f-22d402ee715c");
        quote.setCategory("HOME");
        quote.setTotalMonthlyPremiumAmount(BigDecimal.valueOf(75.25));
        quote.setTotalCoverageAmount(BigDecimal.valueOf(1000.0));
        quote.setCoverages(createCoverages());
        quote.setAssistances(Arrays.asList("Encanador", "Eletricista", "Chaveiro 24h"));
        return quote;
    }

    private ProductDTO createActiveProduct() {
        return ProductDTO.builder()
                .id("1b2da7cc-b367-4196-8a78-9cfeec21f587")
                .name("Seguro de Vida")
                .active(true)
                .build();
    }

    private ProductDTO createInactiveProduct() {
        var p = createActiveProduct();
        p.setActive(false);
        return p;
    }

    private OfferDTO createActiveOffer() {
        return OfferDTO.builder()
                .id("adc56d77-348c-4bf0-908f-22d402ee715c")
                .name("Seguro de Vida Familiar")
                .active(true)
                .coverages(Map.of(
                        "Incêndio", BigDecimal.valueOf(500000.0),
                        "Desastres naturais", BigDecimal.valueOf(600000.0),
                        "Responsabiliadade civil", BigDecimal.valueOf(80000.0),
                        "Roubo", BigDecimal.valueOf(100000.0)
                ))
                .assistances(Arrays.asList("Encanador", "Eletricista", "Chaveiro 24h", "Assistência Funerária"))
                .monthlyPremiumAmount(MonthlyPremiumAmountDTO.builder()
                        .maxAmount(100.74)
                        .minAmount(50.0)
                        .suggestedAmount(60.25)
                        .build())
                .build();
    }

    private OfferDTO createInactiveOffer() {
        var o = createActiveOffer();
        o.setActive(false);
        return o;
    }

    private Map<String, BigDecimal> createCoverages() {
        return new HashMap<>() {
            {
                put("Incêndio", BigDecimal.valueOf(100.0));
                put("Desastres naturais", BigDecimal.valueOf(200.0));
                put("Responsabiliadade civil", BigDecimal.valueOf(700.0));
            }
        };
    }

}

package com.insurance.insurancequote.validator.rule;

import com.insurance.insurancequote.dto.InsuranceQuoteDTO;
import com.insurance.insurancequote.exception.InsuranceQuoteRuleValidationException;
import com.insurance.insurancequote.external.dto.OfferDTO;
import com.insurance.insurancequote.external.dto.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OfferCoveragesRuleHandlerTest {

    private OfferCoveragesRuleHandler ruleHandler;
    private InsuranceQuoteDTO quote;
    private ProductDTO product;
    private OfferDTO offer;

    @BeforeEach
    public void setUp() {
        ruleHandler = new OfferCoveragesRuleHandler();
        quote = mock(InsuranceQuoteDTO.class);
        product = mock(ProductDTO.class);
        offer = mock(OfferDTO.class);
    }

    @Test
    public void testValidate_coverageNotInOffer() {
        Map<String, BigDecimal> quoteCoverages = new HashMap<>();
        quoteCoverages.put("CoverageA", BigDecimal.valueOf(100));
        when(quote.getCoverages()).thenReturn(quoteCoverages);

        Map<String, BigDecimal> offerCoverages = new HashMap<>();
        when(offer.getCoverages()).thenReturn(offerCoverages);

        assertThrows(InsuranceQuoteRuleValidationException.class, () -> {
            ruleHandler.validate(quote, product, offer);
        });
    }

    @Test
    public void testValidate_coverageValueExceedsMax() {
        Map<String, BigDecimal> quoteCoverages = new HashMap<>();
        quoteCoverages.put("CoverageA", BigDecimal.valueOf(200));
        when(quote.getCoverages()).thenReturn(quoteCoverages);

        Map<String, BigDecimal> offerCoverages = new HashMap<>();
        offerCoverages.put("CoverageA", BigDecimal.valueOf(100));
        when(offer.getCoverages()).thenReturn(offerCoverages);

        assertThrows(InsuranceQuoteRuleValidationException.class, () -> {
            ruleHandler.validate(quote, product, offer);
        });
    }

    @Test
    public void testValidate_totalCoverageAmountMismatch() {
        Map<String, BigDecimal> quoteCoverages = new HashMap<>();
        quoteCoverages.put("CoverageA", BigDecimal.valueOf(100));
        when(quote.getCoverages()).thenReturn(quoteCoverages);
        when(quote.getTotalCoverageAmount()).thenReturn(BigDecimal.valueOf(200));

        Map<String, BigDecimal> offerCoverages = new HashMap<>();
        offerCoverages.put("CoverageA", BigDecimal.valueOf(100));
        when(offer.getCoverages()).thenReturn(offerCoverages);

        assertThrows(InsuranceQuoteRuleValidationException.class, () -> {
            ruleHandler.validate(quote, product, offer);
        });
    }

    @Test
    public void testValidate_success() {
        Map<String, BigDecimal> quoteCoverages = new HashMap<>();
        quoteCoverages.put("CoverageA", BigDecimal.valueOf(100));
        when(quote.getCoverages()).thenReturn(quoteCoverages);
        when(quote.getTotalCoverageAmount()).thenReturn(BigDecimal.valueOf(100));

        Map<String, BigDecimal> offerCoverages = new HashMap<>();
        offerCoverages.put("CoverageA", BigDecimal.valueOf(100));
        when(offer.getCoverages()).thenReturn(offerCoverages);

        ruleHandler.validate(quote, product, offer);
    }

    @Test
    public void testValidate_multipleCoverages_success() {
        Map<String, BigDecimal> quoteCoverages = new HashMap<>();
        quoteCoverages.put("CoverageA", BigDecimal.valueOf(100));
        quoteCoverages.put("CoverageB", BigDecimal.valueOf(200));
        when(quote.getCoverages()).thenReturn(quoteCoverages);
        when(quote.getTotalCoverageAmount()).thenReturn(BigDecimal.valueOf(300));

        Map<String, BigDecimal> offerCoverages = new HashMap<>();
        offerCoverages.put("CoverageA", BigDecimal.valueOf(100));
        offerCoverages.put("CoverageB", BigDecimal.valueOf(200));
        when(offer.getCoverages()).thenReturn(offerCoverages);

        ruleHandler.validate(quote, product, offer);
    }

    @Test
    public void testValidate_multipleCoverages_oneExceedsMax() {
        Map<String, BigDecimal> quoteCoverages = new HashMap<>();
        quoteCoverages.put("CoverageA", BigDecimal.valueOf(100));
        quoteCoverages.put("CoverageB", BigDecimal.valueOf(300));
        when(quote.getCoverages()).thenReturn(quoteCoverages);
        when(quote.getTotalCoverageAmount()).thenReturn(BigDecimal.valueOf(400));

        Map<String, BigDecimal> offerCoverages = new HashMap<>();
        offerCoverages.put("CoverageA", BigDecimal.valueOf(100));
        offerCoverages.put("CoverageB", BigDecimal.valueOf(200));
        when(offer.getCoverages()).thenReturn(offerCoverages);

        assertThrows(InsuranceQuoteRuleValidationException.class, () -> {
            ruleHandler.validate(quote, product, offer);
        });
    }

    @Test
    public void testValidate_noCoveragesInQuote() {
        Map<String, BigDecimal> quoteCoverages = new HashMap<>();
        when(quote.getCoverages()).thenReturn(quoteCoverages);
        when(quote.getTotalCoverageAmount()).thenReturn(BigDecimal.ZERO);

        Map<String, BigDecimal> offerCoverages = new HashMap<>();
        offerCoverages.put("CoverageA", BigDecimal.valueOf(100));
        when(offer.getCoverages()).thenReturn(offerCoverages);

        ruleHandler.validate(quote, product, offer);
    }

    @Test
    public void testValidate_noCoveragesInOffer() {
        Map<String, BigDecimal> quoteCoverages = new HashMap<>();
        quoteCoverages.put("CoverageA", BigDecimal.valueOf(100));
        when(quote.getCoverages()).thenReturn(quoteCoverages);
        when(quote.getTotalCoverageAmount()).thenReturn(BigDecimal.valueOf(100));

        Map<String, BigDecimal> offerCoverages = new HashMap<>();
        when(offer.getCoverages()).thenReturn(offerCoverages);

        assertThrows(InsuranceQuoteRuleValidationException.class, () -> {
            ruleHandler.validate(quote, product, offer);
        });
    }
}

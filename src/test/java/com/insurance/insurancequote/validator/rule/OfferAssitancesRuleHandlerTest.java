package com.insurance.insurancequote.validator.rule;

import com.insurance.insurancequote.dto.InsuranceQuoteDTO;
import com.insurance.insurancequote.exception.InsuranceQuoteRuleValidationException;
import com.insurance.insurancequote.external.dto.OfferDTO;
import com.insurance.insurancequote.external.dto.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class OfferAssitancesRuleHandlerTest {

    private OfferAssitancesRuleHandler ruleHandler;
    private InsuranceQuoteDTO quote;
    private ProductDTO product;
    private OfferDTO offer;

    @BeforeEach
    public void setUp() {
        ruleHandler = new OfferAssitancesRuleHandler();
        quote = Mockito.mock(InsuranceQuoteDTO.class);
        product = Mockito.mock(ProductDTO.class);
        offer = Mockito.mock(OfferDTO.class);
    }

    @Test
    public void testValidate_Success() {
        when(quote.getAssistances()).thenReturn(Arrays.asList("Assistance1", "Assistance2"));
        when(offer.getAssistances()).thenReturn(Arrays.asList("Assistance1", "Assistance2", "Assistance3"));

        ruleHandler.validate(quote, product, offer);
    }

    @Test
    public void testValidate_Failure() {
        when(quote.getAssistances()).thenReturn(Arrays.asList("Assistance1", "Assistance4"));
        when(offer.getAssistances()).thenReturn(Arrays.asList("Assistance1", "Assistance2", "Assistance3"));

        assertThrows(InsuranceQuoteRuleValidationException.class, () -> {
            ruleHandler.validate(quote, product, offer);
        });
    }

    @Test
    public void testValidate_EmptyQuoteAssistances() {
        when(quote.getAssistances()).thenReturn(Collections.emptyList());
        when(offer.getAssistances()).thenReturn(Arrays.asList("Assistance1", "Assistance2", "Assistance3"));

        ruleHandler.validate(quote, product, offer);
    }

    @Test
    public void testValidate_EmptyOfferAssistances() {
        when(quote.getAssistances()).thenReturn(Arrays.asList("Assistance1", "Assistance2"));
        when(offer.getAssistances()).thenReturn(Collections.emptyList());

        assertThrows(InsuranceQuoteRuleValidationException.class, () -> {
            ruleHandler.validate(quote, product, offer);
        });
    }
}

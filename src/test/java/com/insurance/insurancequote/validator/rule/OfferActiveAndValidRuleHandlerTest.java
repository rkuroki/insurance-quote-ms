package com.insurance.insurancequote.validator.rule;

import com.insurance.insurancequote.dto.InsuranceQuoteDTO;
import com.insurance.insurancequote.exception.InsuranceQuoteRuleValidationException;
import com.insurance.insurancequote.external.dto.MonthlyPremiumAmountDTO;
import com.insurance.insurancequote.external.dto.OfferDTO;
import com.insurance.insurancequote.external.dto.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OfferActiveAndValidRuleHandlerTest {

    private OfferActiveAndValidRuleHandler ruleHandler;
    private InsuranceQuoteDTO quote;
    private ProductDTO product;
    private OfferDTO offer;

    @BeforeEach
    public void setUp() {
        ruleHandler = new OfferActiveAndValidRuleHandler();
        quote = mock(InsuranceQuoteDTO.class);
        product = mock(ProductDTO.class);
        offer = mock(OfferDTO.class);
    }

    @Test
    public void testValidate_OfferIsValid() {
        when(offer.getActive()).thenReturn(true);
        when(offer.getAssistances()).thenReturn(new ArrayList<>());
        when(offer.getCoverages()).thenReturn(new HashMap<>());
        when(offer.getMonthlyPremiumAmount()).thenReturn(new MonthlyPremiumAmountDTO());

        ruleHandler.validate(quote, product, offer);
    }

    @Test
    public void testValidate_OfferNotActive() {
        when(offer.getActive()).thenReturn(false);

        assertThatThrownBy(() -> ruleHandler.validate(quote, product, offer))
                .isInstanceOf(InsuranceQuoteRuleValidationException.class)
                .hasMessageContaining("Offer is not active.");
    }

    @Test
    public void testValidate_OfferActiveIsNull() {
        when(offer.getActive()).thenReturn(null);

        assertThatThrownBy(() -> ruleHandler.validate(quote, product, offer))
                .isInstanceOf(InsuranceQuoteRuleValidationException.class)
                .hasMessageContaining("Offer is not active.");
    }

    @Test
    public void testValidate_OfferHasNoAssistances() {
        when(offer.getActive()).thenReturn(true);
        when(offer.getAssistances()).thenReturn(null);
        when(offer.getCoverages()).thenReturn(new HashMap<>());

        assertThatThrownBy(() -> ruleHandler.validate(quote, product, offer))
                .isInstanceOf(InsuranceQuoteRuleValidationException.class)
                .hasMessageContaining("Offer does not have assistances.");
    }

    @Test
    public void testValidate_OfferHasNoCoverages() {
        when(offer.getActive()).thenReturn(true);
        when(offer.getAssistances()).thenReturn(new ArrayList<>());
        when(offer.getCoverages()).thenReturn(null);

        assertThatThrownBy(() -> ruleHandler.validate(quote, product, offer))
                .isInstanceOf(InsuranceQuoteRuleValidationException.class)
                .hasMessageContaining("Offer does not have coverages.");
    }

    @Test
    public void testValidate_OfferHasNoMonthlyPremiumAmount() {
        when(offer.getActive()).thenReturn(true);
        when(offer.getAssistances()).thenReturn(new ArrayList<>());
        when(offer.getCoverages()).thenReturn(new HashMap<>());
        when(offer.getMonthlyPremiumAmount()).thenReturn(null);

        assertThatThrownBy(() -> ruleHandler.validate(quote, product, offer))
                .isInstanceOf(InsuranceQuoteRuleValidationException.class)
                .hasMessageContaining("Offer does not have a monthly premium amount.");
    }
}

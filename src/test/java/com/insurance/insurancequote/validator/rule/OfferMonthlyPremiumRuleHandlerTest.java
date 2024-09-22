package com.insurance.insurancequote.validator.rule;

import com.insurance.insurancequote.dto.InsuranceQuoteDTO;
import com.insurance.insurancequote.exception.InsuranceQuoteRuleValidationException;
import com.insurance.insurancequote.external.dto.MonthlyPremiumAmountDTO;
import com.insurance.insurancequote.external.dto.OfferDTO;
import com.insurance.insurancequote.external.dto.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OfferMonthlyPremiumRuleHandlerTest {

    private OfferMonthlyPremiumRuleHandler ruleHandler;
    private InsuranceQuoteDTO quote;
    private ProductDTO product;
    private OfferDTO offer;
    private MonthlyPremiumAmountDTO monthlyPremiumAmount;

    @BeforeEach
    public void setUp() {
        ruleHandler = new OfferMonthlyPremiumRuleHandler();
        quote = Mockito.mock(InsuranceQuoteDTO.class);
        product = Mockito.mock(ProductDTO.class);
        offer = Mockito.mock(OfferDTO.class);
        monthlyPremiumAmount = mock(MonthlyPremiumAmountDTO.class);
    }

    @Test
    public void testValidate_WithinRange() {
        when(offer.getMonthlyPremiumAmount()).thenReturn(monthlyPremiumAmount);
        when(offer.getMonthlyPremiumAmount().getMinAmount()).thenReturn(100.0);
        when(offer.getMonthlyPremiumAmount().getMaxAmount()).thenReturn(200.0);
        when(quote.getTotalMonthlyPremiumAmount()).thenReturn(BigDecimal.valueOf(150.0));

        ruleHandler.validate(quote, product, offer);
    }

    @Test
    public void testValidate_BelowMinRange() {
        when(offer.getMonthlyPremiumAmount()).thenReturn(monthlyPremiumAmount);
        when(offer.getMonthlyPremiumAmount().getMinAmount()).thenReturn(100.00);
        when(offer.getMonthlyPremiumAmount().getMaxAmount()).thenReturn(200.00);
        when(quote.getTotalMonthlyPremiumAmount()).thenReturn(BigDecimal.valueOf(50.00));

        assertThatThrownBy(() -> ruleHandler.validate(quote, product, offer))
                .isInstanceOf(InsuranceQuoteRuleValidationException.class)
                .hasMessageContaining("The monthly premium amount must be between 100.0 and 200.0.");
    }

    @Test
    public void testValidate_AboveMaxRange() {
        when(offer.getMonthlyPremiumAmount()).thenReturn(monthlyPremiumAmount);
        when(offer.getMonthlyPremiumAmount().getMinAmount()).thenReturn(50.0);
        when(offer.getMonthlyPremiumAmount().getMaxAmount()).thenReturn(400.0);
        when(quote.getTotalMonthlyPremiumAmount()).thenReturn(BigDecimal.valueOf(450.0));

        assertThatThrownBy(() -> ruleHandler.validate(quote, product, offer))
                .isInstanceOf(InsuranceQuoteRuleValidationException.class)
                .hasMessageContaining("The monthly premium amount must be between 50.0 and 400.0.");
    }

    @Test
    public void testValidate_AboveMaxRangeInCent() {
        when(offer.getMonthlyPremiumAmount()).thenReturn(monthlyPremiumAmount);
        when(offer.getMonthlyPremiumAmount().getMinAmount()).thenReturn(20.0);
        when(offer.getMonthlyPremiumAmount().getMaxAmount()).thenReturn(30.0);
        when(quote.getTotalMonthlyPremiumAmount()).thenReturn(BigDecimal.valueOf(30.01));

        assertThatThrownBy(() -> ruleHandler.validate(quote, product, offer))
                .isInstanceOf(InsuranceQuoteRuleValidationException.class)
                .hasMessageContaining("The monthly premium amount must be between 20.0 and 30.0.");
    }
}

package com.insurance.insurancequote.validator.rule;

import com.insurance.insurancequote.dto.InsuranceQuoteDTO;
import com.insurance.insurancequote.exception.InsuranceQuoteRuleValidationException;
import com.insurance.insurancequote.external.dto.OfferDTO;
import com.insurance.insurancequote.external.dto.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductActiveRuleHandlerTest {

    private ProductActiveRuleHandler ruleHandler;
    private InsuranceQuoteDTO quote;
    private ProductDTO product;
    private OfferDTO offer;

    @BeforeEach
    public void setUp() {
        ruleHandler = new ProductActiveRuleHandler();
        quote = mock(InsuranceQuoteDTO.class);
        product = mock(ProductDTO.class);
        offer = mock(OfferDTO.class);
    }

    @Test
    public void testValidate_ProductIsActive() {
        when(product.getActive()).thenReturn(true);

        ruleHandler.validate(quote, product, offer);
    }

    @Test
    public void testValidate_ProductNotActive() {
        when(product.getActive()).thenReturn(false);

        assertThatThrownBy(() -> ruleHandler.validate(quote, product, offer))
                .isInstanceOf(InsuranceQuoteRuleValidationException.class)
                .hasMessageContaining("Product is not active.");
    }

    @Test
    public void testValidate_ProductActiveIsNull() {
        when(product.getActive()).thenReturn(null);

        assertThatThrownBy(() -> ruleHandler.validate(quote, product, offer))
                .isInstanceOf(InsuranceQuoteRuleValidationException.class)
                .hasMessageContaining("Product is not active.");
    }

}

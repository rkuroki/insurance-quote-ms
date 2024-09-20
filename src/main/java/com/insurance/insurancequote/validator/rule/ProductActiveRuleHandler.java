package com.insurance.insurancequote.validator.rule;

import com.insurance.insurancequote.dto.InsuranceQuoteDTO;
import com.insurance.insurancequote.external.dto.OfferDTO;
import com.insurance.insurancequote.external.dto.ProductDTO;
import com.insurance.insurancequote.exception.InsuranceQuoteRuleValidationException;

public class ProductActiveRuleHandler implements RuleHandler {

    @Override
    public void validate(InsuranceQuoteDTO quote, ProductDTO product, OfferDTO offer) {
        if (product.getActive() == null || !product.getActive()) {
            throw new InsuranceQuoteRuleValidationException("Product is not active.");
        }
    }
}

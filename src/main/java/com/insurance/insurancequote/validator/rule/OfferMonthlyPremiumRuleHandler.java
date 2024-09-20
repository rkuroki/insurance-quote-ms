package com.insurance.insurancequote.validator.rule;

import java.math.BigDecimal;

import com.insurance.insurancequote.dto.InsuranceQuoteDTO;
import com.insurance.insurancequote.external.dto.OfferDTO;
import com.insurance.insurancequote.external.dto.ProductDTO;
import com.insurance.insurancequote.exception.InsuranceQuoteRuleValidationException;

public class OfferMonthlyPremiumRuleHandler implements RuleHandler {

    @Override
    public void validate(InsuranceQuoteDTO quote, ProductDTO product, OfferDTO offer) {
        var minPremium = BigDecimal.valueOf(offer.getMonthlyPremiumAmount().getMinAmount());
        var maxPremium = BigDecimal.valueOf(offer.getMonthlyPremiumAmount().getMaxAmount());
        var quoteMonthlyPremium = quote.getTotalMonthlyPremiumAmount();

        if (quoteMonthlyPremium.compareTo(minPremium) < 0 || quoteMonthlyPremium.compareTo(maxPremium) > 0) {
            throw new InsuranceQuoteRuleValidationException("The monthly premium amount must be between " + minPremium + " and " + maxPremium + ".");
        }
    }
}

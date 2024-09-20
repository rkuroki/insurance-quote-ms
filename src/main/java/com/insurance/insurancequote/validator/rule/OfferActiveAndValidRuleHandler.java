package com.insurance.insurancequote.validator.rule;

import com.insurance.insurancequote.dto.InsuranceQuoteDTO;
import com.insurance.insurancequote.external.dto.OfferDTO;
import com.insurance.insurancequote.external.dto.ProductDTO;
import com.insurance.insurancequote.exception.InsuranceQuoteRuleValidationException;

public class OfferActiveAndValidRuleHandler implements RuleHandler {

    @Override
    public void validate(InsuranceQuoteDTO quote, ProductDTO product, OfferDTO offer) {
        if (offer.getActive() == null || !offer.getActive()) {
            throw new InsuranceQuoteRuleValidationException("Offer is not active.");
        }
        if (offer.getAssistances() == null) {
            throw new InsuranceQuoteRuleValidationException("Offer does not have assistances.");
        }
        if (offer.getCoverages() == null) {
            throw new InsuranceQuoteRuleValidationException("Offer does not have coverages.");
        }
        if (offer.getMonthlyPremiumAmount() == null) {
            throw new InsuranceQuoteRuleValidationException("Offer does not have a monthly premium amount.");
        }
    }
}

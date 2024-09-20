package com.insurance.insurancequote.validator.rule;

import com.insurance.insurancequote.dto.InsuranceQuoteDTO;
import com.insurance.insurancequote.external.dto.OfferDTO;
import com.insurance.insurancequote.external.dto.ProductDTO;
import com.insurance.insurancequote.exception.InsuranceQuoteRuleValidationException;

public class OfferAssitancesRuleHandler implements RuleHandler {

    @Override
    public void validate(InsuranceQuoteDTO quote, ProductDTO product, OfferDTO offer) {
        var offerAssistances = offer.getAssistances();
        for (String assistance : quote.getAssistances()) {
            if (!offerAssistances.contains(assistance)) {
                throw new InsuranceQuoteRuleValidationException("The assistance [" + assistance + "] is not available in the offer.");
            }
        }
    }
}

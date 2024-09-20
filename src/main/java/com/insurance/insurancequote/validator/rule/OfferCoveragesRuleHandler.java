package com.insurance.insurancequote.validator.rule;

import java.math.BigDecimal;
import java.util.Map;

import com.insurance.insurancequote.dto.InsuranceQuoteDTO;
import com.insurance.insurancequote.external.dto.OfferDTO;
import com.insurance.insurancequote.external.dto.ProductDTO;
import com.insurance.insurancequote.exception.InsuranceQuoteRuleValidationException;

public class OfferCoveragesRuleHandler implements RuleHandler {

    @Override
    public void validate(InsuranceQuoteDTO quote, ProductDTO product, OfferDTO offer) {
        Map<String, BigDecimal> offerCoverages = offer.getCoverages();
        BigDecimal totalCoverageAmount = BigDecimal.ZERO;

        for (Map.Entry<String, BigDecimal> coverageEntry : quote.getCoverages().entrySet()) {
            String coverageName = coverageEntry.getKey();
            BigDecimal coverageValue = coverageEntry.getValue();

            if (!offerCoverages.containsKey(coverageName)) {
                throw new InsuranceQuoteRuleValidationException("The coverage [" + coverageName + "] is not available in the offer.");
            }

            BigDecimal maxCoverageValue = offerCoverages.get(coverageName);
            if (coverageValue.compareTo(maxCoverageValue) > 0) {
                throw new InsuranceQuoteRuleValidationException("The coverage value for [" + coverageName + "] exceeds the maximum allowed.");
            }

            totalCoverageAmount = totalCoverageAmount.add(coverageValue);
        }

        if (totalCoverageAmount.compareTo(quote.getTotalCoverageAmount()) != 0) {
            throw new InsuranceQuoteRuleValidationException("The sum of coverages amount does not match the specified total coverage amount.");
        }
    }
}

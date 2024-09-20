package com.insurance.insurancequote.validator.rule;

import com.insurance.insurancequote.dto.InsuranceQuoteDTO;
import com.insurance.insurancequote.external.dto.OfferDTO;
import com.insurance.insurancequote.external.dto.ProductDTO;

public interface RuleHandler {

    void validate(InsuranceQuoteDTO quote, ProductDTO product, OfferDTO offer);

}

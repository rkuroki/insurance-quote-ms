package com.insurance.insurancequote.validator;

import com.insurance.insurancequote.dto.InsuranceQuoteDTO;
import com.insurance.insurancequote.external.dto.OfferDTO;
import com.insurance.insurancequote.external.dto.ProductDTO;

public interface InsuranceQuoteValidator {

    void validateInsuranceQuote(InsuranceQuoteDTO quote, ProductDTO product, OfferDTO offer);

}

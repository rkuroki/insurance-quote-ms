package com.insurance.insurancequote.service;

import java.util.List;

import com.insurance.insurancequote.dto.InsuranceQuoteDTO;
import com.insurance.insurancequote.entity.InsuranceQuote;

public interface InsuranceQuoteService {

    InsuranceQuoteDTO create(InsuranceQuoteDTO quote);

    InsuranceQuote getInsuranceQuote(Long id);

    void setInsurancePolicy(Long id, Long insurancePolicyId);

    List<InsuranceQuote> getAllInsuranceQuotes();

}

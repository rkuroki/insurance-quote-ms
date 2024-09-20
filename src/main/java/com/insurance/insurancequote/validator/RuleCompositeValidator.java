package com.insurance.insurancequote.validator;

import com.insurance.insurancequote.dto.InsuranceQuoteDTO;
import com.insurance.insurancequote.external.dto.OfferDTO;
import com.insurance.insurancequote.external.dto.ProductDTO;
import com.insurance.insurancequote.validator.rule.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RuleCompositeValidator implements InsuranceQuoteValidator {
    
    public static final List<RuleHandler> DEFAULT_RULE_HANDLERS = List.of(
            new ProductActiveRuleHandler(),
            new OfferActiveAndValidRuleHandler(),
            new OfferCoveragesRuleHandler(),
            new OfferAssitancesRuleHandler(),
            new OfferMonthlyPremiumRuleHandler()
    );

    private final List<RuleHandler> ruleHandlers;

    public RuleCompositeValidator() {
        this(DEFAULT_RULE_HANDLERS);
    }

    public RuleCompositeValidator(List<RuleHandler> ruleHandlers) {
        this.ruleHandlers = ruleHandlers;
    }

    @Override
    public void validateInsuranceQuote(InsuranceQuoteDTO quote, ProductDTO product, OfferDTO offer) {
        for (var handler : ruleHandlers) {
            handler.validate(quote, product, offer);
        }
    }

}

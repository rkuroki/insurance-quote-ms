package com.insurance.insurancequote.messaging.subscriber;

import com.insurance.insurancequote.messaging.dto.InsurancePolicyCreatedEvent;
import com.insurance.insurancequote.service.InsuranceQuoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.insurance.insurancequote.messaging.MessagingConstants.TOPIC_INSURANCE_POLICY_CREATED;

@Component
@Slf4j
public class InsurancePolicyCreationSub {

    private InsuranceQuoteService quoteService;

    public InsurancePolicyCreationSub(InsuranceQuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @KafkaListener(topics = TOPIC_INSURANCE_POLICY_CREATED, groupId = "insurance")
    public void handle(InsurancePolicyCreatedEvent event) {
        log.info(
                "Subscriber received: topic:{} msg: {}",
                TOPIC_INSURANCE_POLICY_CREATED,
                event
        );
        quoteService.setInsurancePolicy(event.insuranceQuoteId(), event.insurancePolicyId());
    }
}

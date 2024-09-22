package com.insurance.insurancequote.xxx.insuransepolicyms;

import com.insurance.insurancequote.messaging.dto.InsurancePolicyCreatedEvent;
import com.insurance.insurancequote.messaging.dto.InsuranceQuoteReceivedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

import static com.insurance.insurancequote.messaging.MessagingConstants.TOPIC_INSURANCE_POLICY_CREATED;
import static com.insurance.insurancequote.messaging.MessagingConstants.TOPIC_INSURANCE_QUOTE_RECEIVED;

// TODO (kuroki): TEMPORARY MOCK SUBSCRIBER (insurance-policy-ms), REMOVE IT.

@Component
@Slf4j
public class PolicyMockSub {

    private final KafkaTemplate<String, InsurancePolicyCreatedEvent> kafkaTemplate;

    public PolicyMockSub(KafkaTemplate<String, InsurancePolicyCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = TOPIC_INSURANCE_QUOTE_RECEIVED, groupId = "insurance")
    public void handle(InsuranceQuoteReceivedEvent event) {
        log.info(
                "### MOCK Subscriber received: topic:{} msg: {}",
                TOPIC_INSURANCE_QUOTE_RECEIVED,
                event
        );
        this.createInsurancePolicy(event);
    }

    private void createInsurancePolicy(InsuranceQuoteReceivedEvent event) {
        Long policyId = ThreadLocalRandom.current().nextLong(1000000L);
        kafkaTemplate.send(TOPIC_INSURANCE_POLICY_CREATED, new InsurancePolicyCreatedEvent(event.insuranceQuoteId(), policyId));
        log.info(
                "### MOCK Publisher sent: topic:{} msg: {}",
                TOPIC_INSURANCE_POLICY_CREATED,
                event
        );
    }
}

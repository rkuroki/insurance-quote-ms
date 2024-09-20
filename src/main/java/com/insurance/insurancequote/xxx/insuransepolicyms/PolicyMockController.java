package com.insurance.insurancequote.xxx.insuransepolicyms;

import com.insurance.insurancequote.messaging.dto.InsurancePolicyCreatedEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ThreadLocalRandom;

import static com.insurance.insurancequote.messaging.MessagingConstants.TOPIC_INSURANCE_POLICY_CREATED;

// TODO: TEMPORARY MOCK CONTROLLER (insurance-policy-ms), REMOVE IT.

@RestController
@RequestMapping("/insurance-policy-ms-mock")
@Validated
public class PolicyMockController {

    private final KafkaTemplate<String, InsurancePolicyCreatedEvent> kafkaTemplate;

    public PolicyMockController(KafkaTemplate<String, InsurancePolicyCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping("/create-force/{quoteId}")
    public ResponseEntity<String> createPolicy(@PathVariable Long quoteId) {
        Long policyId = ThreadLocalRandom.current().nextLong(1000000L);

        kafkaTemplate.send(TOPIC_INSURANCE_POLICY_CREATED, new InsurancePolicyCreatedEvent(quoteId, policyId));

        String result = "quoteId=" + quoteId + " policyId=" + policyId;
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}

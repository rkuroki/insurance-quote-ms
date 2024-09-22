package com.insurance.insurancequote.config;

import com.insurance.insurancequote.messaging.dto.InsurancePolicyCreatedEvent;
import com.insurance.insurancequote.messaging.dto.InsuranceQuoteReceivedEvent;
import com.insurance.insurancequote.messaging.publisher.InsuranceQuoteReceivedPub;
import com.insurance.insurancequote.messaging.subscriber.InsurancePolicyCreationSub;
import com.insurance.insurancequote.xxx.insuransepolicyms.PolicyMockSub;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class MockKafkaConfig {
    @Bean
    public InsuranceQuoteReceivedPub insuranceQuoteReceivedPub() {
        return mock(InsuranceQuoteReceivedPub.class);
    }

    @Bean
    public InsurancePolicyCreationSub insurancePolicyCreationSub() {
        return mock(InsurancePolicyCreationSub.class);
    }

    @Bean
    public KafkaTemplate<String, InsuranceQuoteReceivedEvent> kafkaTemplate() {
        return mock(KafkaTemplate.class);
    }

    // TODO (kuroki): remove insurance-policy-ms mock
    @Bean
    public PolicyMockSub policyMockSub() {
        return mock(PolicyMockSub.class);
    }

    // TODO (kuroki): remove insurance-policy-ms mock
    @Bean
    public KafkaTemplate<String, InsurancePolicyCreatedEvent> policyKafkaTemplate() {
        return mock(KafkaTemplate.class);
    }
}

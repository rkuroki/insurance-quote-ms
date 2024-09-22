package com.insurance.insurancequote.config;

import com.insurance.insurancequote.messaging.dto.InsurancePolicyCreatedEvent;
import com.insurance.insurancequote.messaging.dto.InsuranceQuoteReceivedEvent;
import com.insurance.insurancequote.messaging.publisher.InsuranceQuoteReceivedPubImpl;
import com.insurance.insurancequote.messaging.subscriber.InsurancePolicyCreationSubImpl;
import com.insurance.insurancequote.xxx.insuransepolicyms.PolicyMockSub;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class MockKafkaConfig {

    @Bean
    @Primary
    public InsuranceQuoteReceivedPubImpl insuranceQuoteReceivedPub() {
        return mock(InsuranceQuoteReceivedPubImpl.class);
    }

    @Bean
    @Primary
    public InsurancePolicyCreationSubImpl insurancePolicyCreationSub() {
        return mock(InsurancePolicyCreationSubImpl.class);
    }

    @Bean
    @Primary
    public KafkaTemplate<String, InsuranceQuoteReceivedEvent> kafkaTemplate() {
        return mock(KafkaTemplate.class);
    }

    // TODO (kuroki): remove insurance-policy-ms mock
    @Bean
    @Primary
    public PolicyMockSub policyMockSub() {
        return mock(PolicyMockSub.class);
    }

    // TODO (kuroki): remove insurance-policy-ms mock
    @Bean
    @Primary
    public KafkaTemplate<String, InsurancePolicyCreatedEvent> policyKafkaTemplate() {
        return mock(KafkaTemplate.class);
    }
}

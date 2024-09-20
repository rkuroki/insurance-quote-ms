package com.insurance.insurancequote;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.insurance.insurancequote.messaging.dto.InsurancePolicyCreatedEvent;
import com.insurance.insurancequote.messaging.dto.InsuranceQuoteReceivedEvent;
import com.insurance.insurancequote.messaging.publisher.InsuranceQuoteReceivedPub;
import com.insurance.insurancequote.messaging.subscriber.InsurancePolicyCreationSub;
import com.insurance.insurancequote.xxx.insuransepolicyms.PolicyMockSub;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
class InsuranceQuoteMsApplicationTests {

    @Autowired
    private ApplicationContext context;

    @MockBean
    private InsuranceQuoteReceivedPub insuranceQuoteReceivedPub;

    @MockBean
    private InsurancePolicyCreationSub insurancePolicyCreationSub;

    @MockBean
    private KafkaTemplate<String, InsuranceQuoteReceivedEvent> kafkaTemplate;

    // TODO remove insurance-policy-ms mock
    @MockBean
    private PolicyMockSub policyMockSub;

    // TODO remove insurance-policy-ms mock
    @MockBean
    private KafkaTemplate<String, InsurancePolicyCreatedEvent> policyKafkaTemplate;

    @Test
    void contextLoads() {
        assertNotNull(context);
    }

}

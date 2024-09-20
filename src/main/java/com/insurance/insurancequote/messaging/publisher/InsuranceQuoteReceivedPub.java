package com.insurance.insurancequote.messaging.publisher;

import static com.insurance.insurancequote.messaging.MessagingConstants.TOPIC_INSURANCE_QUOTE_RECEIVED;

import com.insurance.insurancequote.messaging.dto.InsuranceQuoteReceivedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InsuranceQuoteReceivedPub {

    private final KafkaTemplate<String, InsuranceQuoteReceivedEvent> kafkaTemplate;

    public InsuranceQuoteReceivedPub(KafkaTemplate<String, InsuranceQuoteReceivedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(InsuranceQuoteReceivedEvent event) {
        kafkaTemplate.send(TOPIC_INSURANCE_QUOTE_RECEIVED, event);
        log.info(
                "Publisher sent: topic:{} msg: {}",
                TOPIC_INSURANCE_QUOTE_RECEIVED,
                event
        );
    }

}

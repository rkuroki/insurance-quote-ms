package com.insurance.insurancequote.messaging.publisher;

import com.insurance.insurancequote.messaging.dto.InsuranceQuoteReceivedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static com.insurance.insurancequote.messaging.MessagingConstants.TOPIC_INSURANCE_QUOTE_RECEIVED;

@Component
@Slf4j
public class InsuranceQuoteReceivedPubImpl implements InsuranceQuoteReceivedPub {

    private final KafkaTemplate<String, InsuranceQuoteReceivedEvent> kafkaTemplate;

    public InsuranceQuoteReceivedPubImpl(KafkaTemplate<String, InsuranceQuoteReceivedEvent> kafkaTemplate) {
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

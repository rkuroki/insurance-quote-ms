package com.insurance.insurancequote.messaging.publisher;

import com.insurance.insurancequote.messaging.dto.InsuranceQuoteReceivedEvent;

public interface InsuranceQuoteReceivedPub {

    void publish(InsuranceQuoteReceivedEvent event);

}

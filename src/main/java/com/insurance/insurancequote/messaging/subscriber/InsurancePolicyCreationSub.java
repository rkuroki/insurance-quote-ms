package com.insurance.insurancequote.messaging.subscriber;

import com.insurance.insurancequote.messaging.dto.InsurancePolicyCreatedEvent;

public interface InsurancePolicyCreationSub {

    void handle(InsurancePolicyCreatedEvent event);

}

package com.insurance.insurancequote.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.insurance.insurancequote.entity.InsuranceQuoteStatus.RECEIVED;

public class InsuranceQuoteTest {

    private InsuranceQuote insuranceQuote;

    @BeforeEach
    public void setUp() {
        Map<String, BigDecimal> coverages = new HashMap<>();
        coverages.put("Coverage1", new BigDecimal("1000.00"));
        coverages.put("Coverage2", new BigDecimal("2000.00"));

        insuranceQuote = InsuranceQuote.builder()
                .insurancePolicyId(1L)
                .productId("Product1")
                .offerId("Offer1")
                .category("Category1")
                .totalMonthlyPremiumAmount(new BigDecimal("100.00"))
                .totalCoverageAmount(new BigDecimal("3000.00"))
                .coverages(coverages)
                .assistances(List.of("Assistance1", "Assistance2"))
                .customer(new Customer())
                .status(RECEIVED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    public void testInsuranceQuoteCreation() {
        assertNotNull(insuranceQuote);
        assertEquals(1L, insuranceQuote.getInsurancePolicyId());
        assertEquals("Product1", insuranceQuote.getProductId());
        assertEquals("Offer1", insuranceQuote.getOfferId());
        assertEquals("Category1", insuranceQuote.getCategory());
        assertEquals(new BigDecimal("100.00"), insuranceQuote.getTotalMonthlyPremiumAmount());
        assertEquals(new BigDecimal("3000.00"), insuranceQuote.getTotalCoverageAmount());
        assertEquals(2, insuranceQuote.getCoverages().size());
        assertEquals(2, insuranceQuote.getAssistances().size());
        assertNotNull(insuranceQuote.getCustomer());
        assertEquals(RECEIVED, insuranceQuote.getStatus());
        assertNotNull(insuranceQuote.getCreatedAt());
        assertNotNull(insuranceQuote.getUpdatedAt());
    }

    @Test
    public void testPrePersist() throws Exception {
        InsuranceQuote newQuote = new InsuranceQuote();
        Thread.sleep(1000);
        newQuote.onCreate();
        assertNotNull(newQuote.getCreatedAt());
        assertNotNull(newQuote.getUpdatedAt());
        assertEquals(newQuote.getCreatedAt(), newQuote.getUpdatedAt());
    }

    @Test
    public void testPreUpdate() throws Exception {
        LocalDateTime initialTime = insuranceQuote.getUpdatedAt();
        Thread.sleep(1000);
        insuranceQuote.onUpdate();
        assertNotNull(insuranceQuote.getUpdatedAt());
        assertEquals(initialTime, insuranceQuote.getCreatedAt());
        assertNotEquals(initialTime, insuranceQuote.getUpdatedAt());
    }
}

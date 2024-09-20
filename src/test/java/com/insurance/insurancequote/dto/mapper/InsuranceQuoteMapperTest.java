package com.insurance.insurancequote.dto.mapper;

import com.insurance.insurancequote.dto.CustomerDTO;
import com.insurance.insurancequote.dto.InsuranceQuoteDTO;
import com.insurance.insurancequote.entity.Customer;
import com.insurance.insurancequote.entity.InsuranceQuote;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static java.math.BigDecimal.valueOf;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

public class InsuranceQuoteMapperTest {

    private final InsuranceQuoteMapper mapper = Mappers.getMapper(InsuranceQuoteMapper.class);

    @Test
    public void testToEntity() {
        InsuranceQuoteDTO dto = new InsuranceQuoteDTO();
        dto.setId(1L);
        dto.setInsurancePolicyId(323123L);
        dto.setProductId("1b2da7cc-b367-4196-8a78-9cfeec21f587");
        dto.setOfferId("adc56d77-348c-4bf0-908f-22d402ee715c");
        dto.setCategory("HOME");
        dto.setTotalMonthlyPremiumAmount(valueOf(75.25));
        dto.setTotalCoverageAmount(valueOf(825000.0));
        Map<String, BigDecimal> coverages = Map.of(
                "Incêndio", valueOf(250000.0),
                "Desastres naturais", valueOf(500000.0),
                "Responsabiliadade civil", valueOf(75000.0));
        dto.setCoverages(coverages);
        dto.setAssistances(Arrays.asList("Encanador", "Eletricista", "Chaveiro 24h"));
        dto.setCustomer(createFilledCustomerDTO());
        dto.setCreatedAt(LocalDateTime.parse("2021-08-01T00:00:00"));
        dto.setUpdatedAt(LocalDateTime.parse("2021-08-01T00:00:01"));

        InsuranceQuote entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getInsurancePolicyId(), entity.getInsurancePolicyId());
        assertEquals(dto.getProductId(), entity.getProductId());
        assertEquals(dto.getOfferId(), entity.getOfferId());
        assertEquals(dto.getCategory(), entity.getCategory());
        assertEquals(dto.getTotalMonthlyPremiumAmount(), entity.getTotalMonthlyPremiumAmount());
        assertEquals(dto.getTotalCoverageAmount(), entity.getTotalCoverageAmount());
        assertEquals(dto.getCoverages().toString(), entity.getCoverages().toString());
        assertEquals(dto.getAssistances().toString(), entity.getAssistances().toString());
        assertEquals(dto.getCustomer().getDocumentNumber(), entity.getCustomer().getDocumentNumber());
        assertEquals(dto.getCustomer().getName(), entity.getCustomer().getName());
        assertEquals(dto.getCustomer().getType(), entity.getCustomer().getType());
        assertEquals(dto.getCustomer().getGender(), entity.getCustomer().getGender());
        assertEquals(dto.getCustomer().getDateOfBirth(), entity.getCustomer().getDateOfBirth());
        assertEquals(dto.getCustomer().getEmail(), entity.getCustomer().getEmail());
        assertEquals(dto.getCustomer().getPhoneNumber(), entity.getCustomer().getPhoneNumber());
        assertEquals(dto.getCreatedAt(), entity.getCreatedAt());
        assertEquals(dto.getUpdatedAt(), entity.getUpdatedAt());
    }

    @Test
    public void testToDTO() {
        InsuranceQuote entity = new InsuranceQuote();
        entity.setId(1L);
        entity.setInsurancePolicyId(323123L);
        entity.setProductId("1b2da7cc-b367-4196-8a78-9cfeec21f587");
        entity.setOfferId("adc56d77-348c-4bf0-908f-22d402ee715c");
        entity.setCategory("HOME");
        entity.setTotalMonthlyPremiumAmount(valueOf(75.25));
        entity.setTotalCoverageAmount(valueOf(825000.0));
        Map<String, BigDecimal> coverages = Map.of(
                "Incêndio", valueOf(250000.0),
                "Desastres naturais", valueOf(500000.0),
                "Responsabiliadade civil", valueOf(75000.0));
        entity.setCoverages(coverages);
        entity.setAssistances(Arrays.asList("Encanador", "Eletricista", "Chaveiro 24h"));
        entity.setCustomer(createFilledCustomer());
        entity.setCreatedAt(LocalDateTime.parse("2021-08-01T00:00:00"));
        entity.setUpdatedAt(LocalDateTime.parse("2021-08-01T00:00:01"));

        InsuranceQuoteDTO dto = mapper.toDTO(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getInsurancePolicyId(), dto.getInsurancePolicyId());
        assertEquals(entity.getProductId(), dto.getProductId());
        assertEquals(entity.getOfferId(), dto.getOfferId());
        assertEquals(entity.getCategory(), dto.getCategory());
        assertEquals(entity.getTotalMonthlyPremiumAmount(), dto.getTotalMonthlyPremiumAmount());
        assertEquals(entity.getTotalCoverageAmount(), dto.getTotalCoverageAmount());
        assertEquals(entity.getCoverages().toString(), dto.getCoverages().toString());
        assertEquals(entity.getAssistances().toString(), dto.getAssistances().toString());
        assertEquals(entity.getCustomer().getDocumentNumber(), dto.getCustomer().getDocumentNumber());
        assertEquals(entity.getCustomer().getName(), dto.getCustomer().getName());
        assertEquals(entity.getCustomer().getType(), dto.getCustomer().getType());
        assertEquals(entity.getCustomer().getGender(), dto.getCustomer().getGender());
        assertEquals(entity.getCustomer().getDateOfBirth(), dto.getCustomer().getDateOfBirth());
        assertEquals(entity.getCustomer().getEmail(), dto.getCustomer().getEmail());
        assertEquals(entity.getCustomer().getPhoneNumber(), dto.getCustomer().getPhoneNumber());
        assertEquals(entity.getCreatedAt(), dto.getCreatedAt());
        assertEquals(entity.getUpdatedAt(), dto.getUpdatedAt());
    }

    private CustomerDTO createFilledCustomerDTO() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setDocumentNumber("36205578900");
        customerDTO.setName("John Doe");
        customerDTO.setType("NATURAL");
        customerDTO.setGender("MALE");
        customerDTO.setDateOfBirth(LocalDate.of(1999, 1, 31));
        customerDTO.setEmail("johnwick@gmail.com");
        customerDTO.setPhoneNumber("11950503030");
        return customerDTO;
    }

    private Customer createFilledCustomer() {
        var customerMapper = Mappers.getMapper(CustomerMapper.class);
        return customerMapper.toEntity(createFilledCustomerDTO());
    }
}

package com.insurance.insurancequote.config;

import com.insurance.insurancequote.external.catalogms.CatalogMsOfferService;
import com.insurance.insurancequote.external.catalogms.CatalogMsProductService;
import com.insurance.insurancequote.external.dto.MonthlyPremiumAmountDTO;
import com.insurance.insurancequote.external.dto.OfferDTO;
import com.insurance.insurancequote.external.dto.ProductDTO;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.valueOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration
public class MockCatalogServicesConfig {

    @Bean
    @Primary
    public CatalogMsProductService buildProductService() {
        var service = mock(CatalogMsProductService.class);
        when(service.getProductById("1b2da7cc-b367-4196-8a78-9cfeec21f587")).thenReturn(getProductMock());
        return service;
    }

    @Bean
    @Primary
    public CatalogMsOfferService buildOfferService() {
        var service = mock(CatalogMsOfferService.class);
        when(service.getOfferById("adc56d77-348c-4bf0-908f-22d402ee715c")).thenReturn(getOfferMock());
        return service;
    }

    private ProductDTO getProductMock() {
        return ProductDTO.builder()
                .id("1b2da7cc-b367-4196-8a78-9cfeec21f587")
                .name("Seguro de Vida")
                .createdAt(LocalDateTime.parse("2021-07-01T00:00:00"))
                .active(true)
                .offers(Arrays.asList(
                        "adc56d77-348c-4bf0-908f-22d402ee715c",
                        "bdc56d77-348c-4bf0-908f-22d402ee715c",
                        "cdc56d77-348c-4bf0-908f-22d402ee715c"
                ))
                .build();
    }

    private OfferDTO getOfferMock() {
        return OfferDTO.builder()
                .id("adc56d77-348c-4bf0-908f-22d402ee715c")
                .productId("1b2da7cc-b367-4196-8a78-9cfeec21f587")
                .name("Seguro de Vida Familiar")
                .createdAt(LocalDateTime.parse("2020-08-01T00:00:00"))
                .active(true)
                .coverages(Map.of(
                        "Incêndio", valueOf(500000.0),
                        "Desastres naturais", valueOf(600000.0),
                        "Responsabiliadade civil", valueOf(80000.0),
                        "Roubo", valueOf(100000.0)
                ))
                .assistances(List.of("Encanador", "Eletricista", "Chaveiro 24h", "Assistência Funerária"))
                .monthlyPremiumAmount(MonthlyPremiumAmountDTO.builder()
                        .maxAmount(100.74)
                        .minAmount(50.0)
                        .suggestedAmount(60.25)
                        .build())
                .build();
    }
}

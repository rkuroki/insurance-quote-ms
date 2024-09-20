package com.insurance.insurancequote.external.catalogms;

import com.insurance.insurancequote.external.OfferService;
import com.insurance.insurancequote.external.dto.MonthlyPremiumAmountDTO;
import com.insurance.insurancequote.external.dto.OfferDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.valueOf;

@Service
public class CatalogMsOfferService implements OfferService {

    @Value("${catalog-ms.url}")
    private String catalogUrl;

    @Override
    @Cacheable(value = "offers", key = "#id", cacheManager = "cacheManager")
    public OfferDTO getOfferById(String id) {
//        TODO implement
//        RestClient restClient = RestClient.create();
//        return restClient.get()
//                .uri(catalogUrl + "/offers/" + offerId)
//                .retrieve()
//                .body(OfferDTO.class);

        return getMock();
    }

    // TODO: remove it
    private OfferDTO getMock() {
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

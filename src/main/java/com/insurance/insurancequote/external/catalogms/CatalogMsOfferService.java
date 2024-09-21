package com.insurance.insurancequote.external.catalogms;

import com.insurance.insurancequote.external.OfferService;
import com.insurance.insurancequote.external.dto.OfferDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class CatalogMsOfferService implements OfferService {

    @Value("${catalog-ms.url}")
    private String catalogUrl;

    @Override
    @Cacheable(value = "offers", key = "#id", cacheManager = "cacheManager")
    public OfferDTO getOfferById(String id) {
        RestClient restClient = RestClient.create();
        try {
            return restClient.get()
                    .uri(catalogUrl + "/offers/" + id)
                    .retrieve()
                    .body(OfferDTO.class);
        } catch (Exception e) {
            log.error("Error while fetching offer with id: {}", id, e);
            return null;
        }
    }

}

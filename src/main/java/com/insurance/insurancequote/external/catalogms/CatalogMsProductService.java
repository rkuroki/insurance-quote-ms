package com.insurance.insurancequote.external.catalogms;

import com.insurance.insurancequote.external.ProductService;
import com.insurance.insurancequote.external.dto.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class CatalogMsProductService implements ProductService {

    @Value("${catalog-ms.url}")
    private String catalogUrl;

    @Override
    @Cacheable(value = "products", key = "#id", cacheManager = "cacheManager")
    public ProductDTO getProductById(String id) {
        RestClient restClient = RestClient.create();
        try {
            return restClient.get()
                    .uri(catalogUrl + "/products/" + id)
                    .retrieve()
                    .body(ProductDTO.class);
        } catch (Exception e) {
            log.error("Error while fetching product with id: {}", id, e);
            return null;
        }
    }

}

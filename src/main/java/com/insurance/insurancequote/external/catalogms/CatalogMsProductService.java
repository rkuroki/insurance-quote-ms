package com.insurance.insurancequote.external.catalogms;

import com.insurance.insurancequote.external.ProductService;
import com.insurance.insurancequote.external.dto.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@Slf4j
public class CatalogMsProductService implements ProductService {

    @Value("${catalog-ms.url}")
    private String catalogUrl;

    @Override
    @Cacheable(value = "products", key = "#id", cacheManager = "cacheManager")
    public ProductDTO getProductById(String id) {
//        TODO implement
//        RestClient restClient = RestClient.create();
//        return restClient.get()
//                .uri(catalogUrl + "/products/" + productId)
//                .retrieve()
//                .body(ProductDTO.class);

        return getMock();
    }

    // TODO: remove it
    private ProductDTO getMock() {
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

}

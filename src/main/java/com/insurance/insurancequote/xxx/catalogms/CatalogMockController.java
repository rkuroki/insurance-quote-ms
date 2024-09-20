package com.insurance.insurancequote.xxx.catalogms;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.insurancequote.xxx.catalogms.dto.CatalogOfferDTO;
import com.insurance.insurancequote.xxx.catalogms.dto.CatalogProductDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

// TODO: TEMPORARY MOCK CONTROLLER (catalog-ms), REMOVE IT.

@RestController
@RequestMapping("/catalog-ms-mock")
@Validated
public class CatalogMockController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final List<CatalogProductDTO> products;
    private final List<CatalogOfferDTO> offers;

    public CatalogMockController() {
        this.products = loadProducts();
        this.offers = loadOffers();
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<CatalogProductDTO> getProduct(@PathVariable(required = true) String productId) {
        var product = this.products.stream()
                .filter(p -> productId.equals(p.getId()))
                .findFirst()
                .orElse(null);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @GetMapping("/offers/{offerId}")
    public ResponseEntity<CatalogOfferDTO> getOffer(@PathVariable(required = true) String offerId) {
        var offer = this.offers.stream()
                .filter(p -> offerId.equals(p.getId()))
                .findFirst()
                .orElse(null);
        return new ResponseEntity<>(offer, HttpStatus.CREATED);
    }

    private List<CatalogProductDTO> loadProducts() throws RuntimeException {
        try {
            return objectMapper.readValue(
                    Paths.get("src/main/resources/xxx/products.json").toFile(),
                    new TypeReference<List<CatalogProductDTO>>() {}
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<CatalogOfferDTO> loadOffers() throws RuntimeException {
        try {
            return objectMapper.readValue(
                    Paths.get("src/main/resources/xxx/offers.json").toFile(),
                    new TypeReference<List<CatalogOfferDTO>>() {}
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}



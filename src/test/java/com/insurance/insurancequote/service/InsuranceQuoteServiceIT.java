package com.insurance.insurancequote.service;

public class InsuranceQuoteServiceIT {
    // TODO: Implement integration tests for InsuranceQuoteService
}

/*
package com.insurance.insurancequote.service;

import com.insurance.insurancequote.dto.InsuranceQuoteDTO;
import com.insurance.insurancequote.external.dto.ProductDTO;
import com.insurance.insurancequote.external.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Testcontainers
@Transactional
class InsuranceQuoteServiceIT {

    @Autowired
    private InsuranceQuoteService insuranceQuoteService;

    @Autowired
    private ProductService productService;

    @Container
    private static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.2")
            .withDatabaseName("insurance_quote_db")
            .withUsername("postgres")
            .withPassword("password");

    @DynamicPropertySource
    static void configureProperties(org.springframework.test.context.DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        // Simulate a ProductService with a mock external API call.
        ProductService mockProductService = mock(ProductService.class);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId("validProductId");
        when(mockProductService.getProductById("validProductId")).thenReturn(productDTO);
    }

    @Test
    void create_whenProductExists_shouldPersistInsuranceQuote() {
        // Arrange
        InsuranceQuoteDTO quoteRequest = new InsuranceQuoteDTO();
        quoteRequest.setProductId("validProductId");

        // Act
        InsuranceQuoteDTO result = insuranceQuoteService.create(quoteRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getProductId()).isEqualTo("validProductId");
    }

    @Test
    void create_whenProductDoesNotExist_shouldThrowEntityNotFoundException() {
        // Arrange
        InsuranceQuoteDTO quoteRequest = new InsuranceQuoteDTO();
        quoteRequest.setProductId("invalidProductId");

        when(productService.getProductById("invalidProductId")).thenReturn(null);

        // Act & Assert
        assertThatThrownBy(() -> insuranceQuoteService.create(quoteRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Product not found with id: invalidProductId");
    }
}
 */

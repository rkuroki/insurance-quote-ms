package com.insurance.insurancequote.service;

import com.insurance.insurancequote.dto.InsuranceQuoteDTO;
import com.insurance.insurancequote.entity.InsuranceQuote;
import com.insurance.insurancequote.entity.InsuranceQuoteStatus;
import com.insurance.insurancequote.external.OfferService;
import com.insurance.insurancequote.external.ProductService;
import com.insurance.insurancequote.external.dto.OfferDTO;
import com.insurance.insurancequote.external.dto.ProductDTO;
import com.insurance.insurancequote.messaging.publisher.InsuranceQuoteReceivedPub;
import com.insurance.insurancequote.repository.InsuranceQuoteRepository;
import com.insurance.insurancequote.validator.InsuranceQuoteValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Map;

import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class InsuranceQuoteServiceTest {

    @Mock
    private InsuranceQuoteRepository repository;

    @Mock
    private ProductService productService;

    @Mock
    private OfferService offerService;

    @Mock
    private InsuranceQuoteValidator validator;

    @Mock
    private InsuranceQuoteReceivedPub quoteReceivedPub;

    @InjectMocks
    private InsuranceQuoteServiceImpl insuranceQuoteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_whenQuoteIsValid_shouldReturnInsuranceQuote() {
        // Arrange
        String productId = "validProductId";
        ProductDTO productDTO = createProductDTO();
        productDTO.setId(productId);

        String offerId = "validOfferId";
        OfferDTO offerDTO = createOfferDTO();
        offerDTO.setId(offerId);

        InsuranceQuoteDTO quoteDTO = createInsuranceQuoteDTO();
        quoteDTO.setProductId(productId);
        quoteDTO.setOfferId(offerId);

        when(productService.getProductById(productId)).thenReturn(productDTO);
        when(offerService.getOfferById(offerId)).thenReturn(offerDTO);
        doNothing().when(quoteReceivedPub).publish(any());

        doNothing().when(validator).validateInsuranceQuote(any(), any(), any());

        when(repository.save(any())).thenAnswer(answer -> {
            InsuranceQuote quote = answer.getArgument(0);
            quote.setId(1L);
            return quote;
        });

        // Act
        InsuranceQuoteDTO result = insuranceQuoteService.create(quoteDTO);

        // Assert
        assertThat(result).isNotNull();
        assertEquals(1L, result.getId());
        assertEquals(InsuranceQuoteStatus.RECEIVED, result.getStatus());

        verify(productService, times(1)).getProductById(productId);
        verify(offerService, times(1)).getOfferById(offerId);
    }

    @Test
    void create_whenProductDoesNotExist_shouldThrowIllegalArgumentException() {
        // Arrange
        String invalidProductId = "invalidProductId";

        InsuranceQuoteDTO quoteRequest = new InsuranceQuoteDTO();
        quoteRequest.setProductId(invalidProductId);

        when(productService.getProductById(invalidProductId)).thenReturn(null);

        // Act and Assert
        assertThatThrownBy(() -> insuranceQuoteService.create(quoteRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product not found with id: " + invalidProductId);

        verify(productService, times(1)).getProductById(invalidProductId);
    }

    @Test
    void create_whenQuoteIsValid_failedPublishigCreatedMsg_shouldReturnInsuranceQuoteWithFailed() {
        // Arrange
        String productId = "validProductId";
        ProductDTO productDTO = createProductDTO();
        productDTO.setId(productId);

        String offerId = "validOfferId";
        OfferDTO offerDTO = createOfferDTO();
        offerDTO.setId(offerId);

        InsuranceQuoteDTO quoteDTO = createInsuranceQuoteDTO();
        quoteDTO.setProductId(productId);
        quoteDTO.setOfferId(offerId);

        when(productService.getProductById(productId)).thenReturn(productDTO);
        when(offerService.getOfferById(offerId)).thenReturn(offerDTO);
        doThrow(new RuntimeException("Publishing to topic exeception")).when(quoteReceivedPub).publish(any());

        doNothing().when(validator).validateInsuranceQuote(any(), any(), any());

        when(repository.save(any())).thenAnswer(answer -> {
            InsuranceQuote quote = answer.getArgument(0);
            quote.setId(1L);
            return quote;
        });

        // Act
        InsuranceQuoteDTO result = insuranceQuoteService.create(quoteDTO);

        // Assert
        assertThat(result).isNotNull();
        assertEquals(1L, result.getId());
        assertEquals(InsuranceQuoteStatus.FAILED, result.getStatus());

        verify(productService, times(1)).getProductById(productId);
        verify(offerService, times(1)).getOfferById(offerId);
    }

    private Map<String, BigDecimal> createCoverages() {
        return Map.of(
                "Incêndio", valueOf(500.0),
                "Desastres naturais", valueOf(300.0),
                "Responsabiliadade civil", valueOf(200.0)
        );
    }

    private InsuranceQuoteDTO createInsuranceQuoteDTO() {
        return InsuranceQuoteDTO.builder()
                .productId("c8a1aefa-25d9-43ce-8eee-da4aace4a7d4")
                .offerId("cb957c1b-9952-4a79-b0d2-ad2adfe42020")
                .totalCoverageAmount(valueOf(1000.00))
                .coverages(createCoverages())
                .build();
    }

    private ProductDTO createProductDTO() {
        return ProductDTO.builder()
                .id("c8a1aefa-25d9-43ce-8eee-da4aace4a7d4")
                .active(true)
                .build();
    }

    private OfferDTO createOfferDTO() {
        return OfferDTO.builder()
                .id("cb957c1b-9952-4a79-b0d2-ad2adfe42020")
                .active(true)
                .coverages(createCoverages())
                .build();
    }
}

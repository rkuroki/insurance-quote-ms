package com.insurance.insurancequote.external.catalogms;

import com.insurance.insurancequote.config.MockKafkaConfig;
import com.insurance.insurancequote.external.dto.OfferDTO;
import com.insurance.insurancequote.external.dto.ProductDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;

import static com.insurance.insurancequote.config.TestcontaionerAppConstants.MOCKSERVER_IMAGE;
import static org.junit.Assert.*;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import({MockKafkaConfig.class})
public class CatalogMsContainerTest {

    @Autowired
    private CatalogMsProductService productService;

    @Autowired
    private CatalogMsOfferService offerService;

    @Container
    public static MockServerContainer mockServerContainer = new MockServerContainer(
            DockerImageName.parse(MOCKSERVER_IMAGE)
    )
            .withClasspathResourceMapping(
                    "testcontainer/catalog-ms-mockserver/catalog-ms-mock-data.json",
                    "/config/init_expectations.json",
                    org.testcontainers.containers.BindMode.READ_ONLY)
            .withEnv("MOCKSERVER_INITIALIZATION_JSON_PATH", "/config/init_expectations.json");

    @DynamicPropertySource
    static void overrideCatalogMsProperties(DynamicPropertyRegistry registry) {
        String catalogUrl = String.format("http://%s:%d/catalog-ms-mock",
                mockServerContainer.getHost(),
                mockServerContainer.getServerPort());
        registry.add("catalog-ms.url", () -> catalogUrl);
    }

    @Test
    public void getProductFromMockserver_checkFields() {
        // Arrange and Act
        String productId = "8c71f0a2-7351-4a90-a84c-ef376f481cb5"; // Seguro Viagem
        ProductDTO product = productService.getProductById(productId);

        // Assert
        assertEquals("Seguro Viagem", product.getName());
        assertEquals("8c71f0a2-7351-4a90-a84c-ef376f481cb5", product.getId());
        assertTrue(product.getActive());
        assertEquals(3, product.getOffers().size());
        assertTrue(product.getOffers().contains("41e54e5a-f5de-4560-bc4b-5fa43a61cb29"));
        assertTrue(product.getOffers().contains("ae91fc82-e408-41db-a657-3b1f2d5cfa2c"));
        assertTrue(product.getOffers().contains("21d7b9b2-f6d3-49b5-a697-6e5c5c2e4b9d"));
    }

    @Test
    public void getProductFromMockserver_notFound() {
        // Arrange and Act
        String productId = "8c71f0a2-7351-4a90-a84c-ef376f481cb5 bla bla";
        ProductDTO product = productService.getProductById(productId);

        // Assert
        assertNull(product);
    }

    @Test
    public void getOfferFromMockserver_checkFields() {
        // Arrange and Act
        String offerId = "41e54e5a-f5de-4560-bc4b-5fa43a61cb29"; // Seguro Viagem Nacional
        OfferDTO offer = offerService.getOfferById(offerId);

        // Assert
        assertEquals("Seguro Viagem Nacional", offer.getName());
        assertEquals("41e54e5a-f5de-4560-bc4b-5fa43a61cb29", offer.getId());
        assertEquals("8c71f0a2-7351-4a90-a84c-ef376f481cb5", offer.getProductId());
        assertTrue(offer.getActive());
        assertEquals(4, offer.getCoverages().size());
        assertEquals(BigDecimal.valueOf(1000.0), offer.getCoverages().get("Acidentes pessoais"));
        assertEquals(BigDecimal.valueOf(2000.0), offer.getCoverages().get("Assistência médica"));
        assertEquals(BigDecimal.valueOf(3000.0), offer.getCoverages().get("Cancelamento de viagem"));
        assertEquals(BigDecimal.valueOf(4000.0), offer.getCoverages().get("Extravio de bagagem"));
        assertEquals(2, offer.getAssistances().size());
        assertTrue(offer.getAssistances().contains("Suporte em viagem"));
        assertTrue(offer.getAssistances().contains("Translado médico"));
        assertEquals(75.5, offer.getMonthlyPremiumAmount().getMaxAmount(), 0.0);
        assertEquals(35.0, offer.getMonthlyPremiumAmount().getMinAmount(), 0.0);
        assertEquals(45.0, offer.getMonthlyPremiumAmount().getSuggestedAmount(), 0.0);
    }

    @Test
    public void getOfferFromMockserver_notFound() {
        // Arrange and Act
        String offerId = "41e54e5a-f5de-4560-bc4b-5fa43a61cb29 bla bla";
        OfferDTO offer = offerService.getOfferById(offerId);

        // Assert
        assertNull(offer);
    }
}

package com.insurance.insurancequote.controller;

import com.insurance.insurancequote.repository.InsuranceQuoteRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

import static com.insurance.insurancequote.config.TestcontaionerAppConstants.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class InsuranceQuoteControllerContainerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InsuranceQuoteRepository insuranceQuoteRepository;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(POSTGRESQL_IMAGE)
            .withDatabaseName("insurance")
            .withUsername("insuranceContainer")
            .withPassword("insuranceContainer");

    @DynamicPropertySource
    static void overridePgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Container
    static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse(KAFKA_IMAGE)
    );

    @DynamicPropertySource
    static void overrideKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.kafka.consumer.auto-offset-reset", () -> "earliest");
    }

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

    @BeforeEach
    void setUp() {
        insuranceQuoteRepository.deleteAll();
    }

    @Test
    public void saveAndGetQuote() throws Exception {
        String quoteRequest = """
                    {
                      "product_id": "1b2da7cc-b367-4196-8a78-9cfeec21f587",
                      "offer_id": "adc56d77-348c-4bf0-908f-22d402ee715c",
                      "category": "HOME",
                      "total_monthly_premium_amount": 75.25,
                      "total_coverage_amount": 825000.00,
                      "coverages": {
                          "Incêndio": 250000.00,
                          "Desastres naturais": 500000.00,
                          "Responsabiliadade civil": 75000.00
                      },
                      "assistances": [
                          "Encanador",
                          "Eletricista",
                          "Chaveiro 24h"
                      ],
                      "customer": {
                          "document_number": "36205578900",
                          "name": "John Wick",
                          "type": "NATURAL",
                          "gender": "MALE",
                          "date_of_birth": "1973-05-02",
                          "email": "johnwick@gmail.com",
                          "phone_number": 11950503030
                      }
                    }
                """;

        // Saving Quote Request
        MvcResult result = mockMvc.perform(post("/insurance-quote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(quoteRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", isA(Number.class)))
                .andExpect(jsonPath("$.product_id", is("1b2da7cc-b367-4196-8a78-9cfeec21f587")))
                .andExpect(jsonPath("$.offer_id", is("adc56d77-348c-4bf0-908f-22d402ee715c")))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Long id = ((Number) JsonPath.read(jsonResponse, "$.id")).longValue();

        // Getting
        mockMvc.perform(get("/insurance-quote/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(quoteRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", isA(Number.class)))
                .andExpect(jsonPath("$.product_id", is("1b2da7cc-b367-4196-8a78-9cfeec21f587")))
                .andExpect(jsonPath("$.offer_id", is("adc56d77-348c-4bf0-908f-22d402ee715c")))
                .andExpect(jsonPath("$.category", is("HOME")))
                .andExpect(jsonPath("$.total_monthly_premium_amount", is(75.25)))
                .andExpect(jsonPath("$.total_coverage_amount", is(825000.00)))
                .andExpect(jsonPath("$.coverages", aMapWithSize(3)))
                .andExpect(jsonPath("$.coverages.['Incêndio']", is(250000.00)))
                .andExpect(jsonPath("$.coverages.['Desastres naturais']", is(500000.00)))
                .andExpect(jsonPath("$.coverages.['Responsabiliadade civil']", is(75000.00)))
                .andExpect(jsonPath("$.assistances", hasSize(3)))
                .andExpect(jsonPath("$.assistances[0]", is("Encanador")))
                .andExpect(jsonPath("$.assistances[1]", is("Eletricista")))
                .andExpect(jsonPath("$.assistances[2]", is("Chaveiro 24h")))
                .andExpect(jsonPath("$.customer.document_number", is("36205578900")))
                .andExpect(jsonPath("$.customer.name", is("John Wick")))
                .andExpect(jsonPath("$.customer.type", is("NATURAL")))
                .andExpect(jsonPath("$.customer.gender", is("MALE")))
                .andExpect(jsonPath("$.customer.date_of_birth", is("1973-05-02")))
                .andExpect(jsonPath("$.customer.email", is("johnwick@gmail.com")))
                .andExpect(jsonPath("$.customer.phone_number", is("11950503030")));

        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    mockMvc.perform(get("/insurance-quote/" + id)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(quoteRequest))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.id", isA(Number.class)))
                            .andExpect(jsonPath("$.insurance_policy_id", isA(Number.class)));
                });
    }

    @Test
    public void saveAndGetQuote_otherQuote() throws Exception {
        String quoteRequest = """
                    {
                      "product_id": "8c71f0a2-7351-4a90-a84c-ef376f481cb5",
                      "offer_id": "ae91fc82-e408-41db-a657-3b1f2d5cfa2c",
                      "category": "TRAVEL",
                      "total_monthly_premium_amount": 90.70,
                      "total_coverage_amount": 85000.00,
                      "coverages": {
                          "Cancelamento de viagem": 25000.00,
                          "Extravio de bagagem": 38000.00,
                          "Assistência médica": 22000.00
                      },
                      "assistances": [
                          "Repatriação sanitária",
                          "Assistência jurídica"
                      ],
                      "customer": {
                          "document_number": "4839843983",
                          "name": "João do Teste",
                          "type": "ARTIFICIAL",
                          "gender": "FEMALE",
                          "date_of_birth": "1923-01-31",
                          "email": "joaodoteste@gmail.com",
                          "phone_number": 11988887777
                      }
                    }
                """;

        // Saving Quote Request
        MvcResult result = mockMvc.perform(post("/insurance-quote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(quoteRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", isA(Number.class)))
                .andExpect(jsonPath("$.product_id", is("8c71f0a2-7351-4a90-a84c-ef376f481cb5")))
                .andExpect(jsonPath("$.offer_id", is("ae91fc82-e408-41db-a657-3b1f2d5cfa2c")))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Long id = ((Number) JsonPath.read(jsonResponse, "$.id")).longValue();

        // Getting
        mockMvc.perform(get("/insurance-quote/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(quoteRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", isA(Number.class)))
                .andExpect(jsonPath("$.product_id", is("8c71f0a2-7351-4a90-a84c-ef376f481cb5")))
                .andExpect(jsonPath("$.offer_id", is("ae91fc82-e408-41db-a657-3b1f2d5cfa2c")))
                .andExpect(jsonPath("$.category", is("TRAVEL")))
                .andExpect(jsonPath("$.total_monthly_premium_amount", is(90.70)))
                .andExpect(jsonPath("$.total_coverage_amount", is(85000.00)))
                .andExpect(jsonPath("$.coverages", aMapWithSize(3)))
                .andExpect(jsonPath("$.coverages.['Cancelamento de viagem']", is(25000.00)))
                .andExpect(jsonPath("$.coverages.['Extravio de bagagem']", is(38000.00)))
                .andExpect(jsonPath("$.coverages.['Assistência médica']", is(22000.00)))
                .andExpect(jsonPath("$.assistances", hasSize(2)))
                .andExpect(jsonPath("$.assistances[0]", is("Repatriação sanitária")))
                .andExpect(jsonPath("$.assistances[1]", is("Assistência jurídica")))
                .andExpect(jsonPath("$.customer.document_number", is("4839843983")))
                .andExpect(jsonPath("$.customer.name", is("João do Teste")))
                .andExpect(jsonPath("$.customer.type", is("ARTIFICIAL")))
                .andExpect(jsonPath("$.customer.gender", is("FEMALE")))
                .andExpect(jsonPath("$.customer.date_of_birth", is("1923-01-31")))
                .andExpect(jsonPath("$.customer.email", is("joaodoteste@gmail.com")))
                .andExpect(jsonPath("$.customer.phone_number", is("11988887777")));

        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    mockMvc.perform(get("/insurance-quote/" + id)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(quoteRequest))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.id", isA(Number.class)))
                            .andExpect(jsonPath("$.insurance_policy_id", isA(Number.class)));
                });
    }

}

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
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

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
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("insurance")
            .withUsername("insuranceContainer")
            .withPassword("insuranceContainer");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Container
    static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:latest")
    );

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.kafka.consumer.auto-offset-reset", () -> "earliest");
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

        // Getting after policy creation
        Thread.sleep(5000); // TODO REMOVE IT!!!! (config and use awaitility)
        mockMvc.perform(get("/insurance-quote/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(quoteRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", isA(Number.class)))
                .andExpect(jsonPath("$.insurance_policy_id", isA(Number.class)));
    }

}

package com.insurance.insurancequote.controller;

import com.insurance.insurancequote.config.MockCatalogServicesConfig;
import com.insurance.insurancequote.config.MockKafkaConfig;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import({MockKafkaConfig.class, MockCatalogServicesConfig.class})
public class InsuranceQuoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

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

        // Request/save new quote
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

        // Get saved quote
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
    }

    @Test
    public void shouldReturnBadRequest_whenRequiredFieldIsMissing() throws Exception {
        String quoteRequest = """
                    {
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

        mockMvc.perform(post("/insurance-quote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(quoteRequest))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.statusCode", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", is("Invalid payload, check 'details'.")))
                .andExpect(jsonPath("$.details.productId", is("must not be null")));
    }

    /**
     * Checking validation rules like: "total_coverage_amount != sum(coverages)"
     */
    @Test
    public void shouldReturnUnprocessableEntity_whenBusinessRulesAreViolated() throws Exception {
        String quoteRequest = """
                    {
                      "product_id": "1b2da7cc-b367-4196-8a78-9cfeec21f587",
                      "offer_id": "adc56d77-348c-4bf0-908f-22d402ee715c",
                      "category": "HOME",
                      "total_monthly_premium_amount": 75.25,
                      "total_coverage_amount": 1000.00,
                      "coverages": {
                          "Incêndio": 1.00,
                          "Desastres naturais": 2.00,
                          "Responsabiliadade civil": 3.00
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

        mockMvc.perform(post("/insurance-quote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(quoteRequest))
                .andExpect(status().is(422))
                .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")))
                .andExpect(jsonPath("$.message", is("The sum of coverages amount does not match the specified total coverage amount.")))
                .andExpect(jsonPath("$.details", nullValue()));
    }

    @Test
    public void shouldReturnUnprocessableEntity_whenACoverageIsInvalid() throws Exception {
        String quoteRequest = """
                    {
                      "product_id": "1b2da7cc-b367-4196-8a78-9cfeec21f587",
                      "offer_id": "adc56d77-348c-4bf0-908f-22d402ee715c",
                      "category": "HOME",
                      "total_monthly_premium_amount": 75.25,
                      "total_coverage_amount": 1000.00,
                      "coverages": {
                          "Incêndio": 1.00,
                          "Desastres naturais": 2.00,
                          "Responsabiliadade civil": 30000000000.00
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

        mockMvc.perform(post("/insurance-quote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(quoteRequest))
                .andExpect(status().is(422))
                .andExpect(jsonPath("$.statusCode", is("UNPROCESSABLE_ENTITY")))
                .andExpect(jsonPath("$.message", is("The coverage value for [Responsabiliadade civil] exceeds the maximum allowed.")))
                .andExpect(jsonPath("$.details", nullValue()));
    }

}

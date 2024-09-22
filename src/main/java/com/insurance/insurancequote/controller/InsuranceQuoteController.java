package com.insurance.insurancequote.controller;

import com.insurance.insurancequote.dto.InsuranceQuoteDTO;
import com.insurance.insurancequote.dto.InsuranceQuoteRequestDTO;
import com.insurance.insurancequote.dto.mapper.InsuranceQuoteMapper;
import com.insurance.insurancequote.service.InsuranceQuoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/insurance-quote")
@Validated
public class InsuranceQuoteController {

    private final InsuranceQuoteMapper mapper = InsuranceQuoteMapper.INSTANCE;

    private final InsuranceQuoteService service;

    public InsuranceQuoteController(InsuranceQuoteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<InsuranceQuoteDTO> create(@Valid @RequestBody InsuranceQuoteRequestDTO request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsuranceQuoteDTO> getInsuranceQuote(@PathVariable Long id) {
        var quote = mapper.toDTO(service.getInsuranceQuote(id));
        return new ResponseEntity<>(quote, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<InsuranceQuoteDTO>> getAllInsuranceQuotes() {
        var result = service.getAllInsuranceQuotes()
                .stream()
                .map(mapper::toDTO)
                .toList();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}

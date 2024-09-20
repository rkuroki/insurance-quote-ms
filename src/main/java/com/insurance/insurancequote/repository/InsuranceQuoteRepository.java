package com.insurance.insurancequote.repository;

import com.insurance.insurancequote.entity.InsuranceQuote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceQuoteRepository extends JpaRepository<InsuranceQuote, Long> {
}
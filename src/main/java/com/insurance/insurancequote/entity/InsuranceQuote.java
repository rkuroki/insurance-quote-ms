package com.insurance.insurancequote.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "insurance_quote")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InsuranceQuote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private Long insurancePolicyId;

    private String productId;
    private String offerId;
    private String category;
    private BigDecimal totalMonthlyPremiumAmount;
    private BigDecimal totalCoverageAmount;

    @ElementCollection
    @CollectionTable(name = "insurance_coverage", joinColumns = @JoinColumn(name = "insurance_quote_id"))
    @MapKeyColumn(name = "coverage_name")
    @Column(name = "coverage_amount")
    private Map<String, BigDecimal> coverages;

    @ElementCollection
    @CollectionTable(name = "insurance_assistance", joinColumns = @JoinColumn(name = "insurance_quote_id"))
    @Column(name = "assistance_name")
    private List<String> assistances;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", nullable = true)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private InsuranceQuoteStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

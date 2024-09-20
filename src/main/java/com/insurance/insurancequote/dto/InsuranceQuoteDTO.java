package com.insurance.insurancequote.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.insurance.insurancequote.entity.InsuranceQuoteStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InsuranceQuoteDTO {

    private Long id;

    private Long insurancePolicyId;

    @NotNull
    private String productId;

    @NotNull
    private String offerId;

    private String category;

    @NotNull
    private BigDecimal totalMonthlyPremiumAmount;

    @NotNull
    private BigDecimal totalCoverageAmount;

    @NotEmpty
    private Map<String, BigDecimal> coverages;

    @NotEmpty
    private List<String> assistances;

    private CustomerDTO customer;

    private InsuranceQuoteStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime updatedAt;

}

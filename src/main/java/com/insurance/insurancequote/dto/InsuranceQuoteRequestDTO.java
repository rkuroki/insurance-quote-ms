package com.insurance.insurancequote.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.insurance.insurancequote.entity.InsuranceQuoteStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InsuranceQuoteRequestDTO extends InsuranceQuoteDTO {

    @JsonIgnore
    private Long id;

    @JsonIgnore
    private Long insurancePolicyId;

    @JsonIgnore
    private InsuranceQuoteStatus status;

    @JsonIgnore
    private LocalDateTime createdAt;

    @JsonIgnore
    private LocalDateTime updatedAt;

}

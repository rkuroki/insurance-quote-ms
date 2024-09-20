package com.insurance.insurancequote.xxx.catalogms.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class CatalogOfferDTO {
    private String id;
    private String product_id;
    private String name;
    private String created_at;
    private Boolean active;
    private Map<String, BigDecimal> coverages;
    private List<String> assistances;
    private CatalogMonthlyPremiumAmountDTO monthly_premium_amount;
}

package com.insurance.insurancequote.xxx.catalogms.dto;

import lombok.Data;

import java.util.List;

@Data
public class CatalogProductDTO {
    private String id;
    private String name;
    private String created_at;
    private Boolean active;
    private List<String> offers;
}

package com.insurance.insurancequote.external;

import com.insurance.insurancequote.external.dto.ProductDTO;
import jakarta.annotation.Nullable;

public interface ProductService {

    @Nullable
    ProductDTO getProductById(String productId);

}

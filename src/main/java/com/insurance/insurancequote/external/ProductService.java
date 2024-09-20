package com.insurance.insurancequote.external;

import com.insurance.insurancequote.external.dto.ProductDTO;

public interface ProductService {

    ProductDTO getProductById(String productId);

}

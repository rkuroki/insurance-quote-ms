package com.insurance.insurancequote.external;

import com.insurance.insurancequote.external.catalogms.CatalogMsProductService;
import com.insurance.insurancequote.external.dto.ProductDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductServiceTest {

    @InjectMocks
    private final ProductService productService = new CatalogMsProductService();

    @Test
    public void testGetProductById() {
        ProductDTO product = productService.getProductById("1b2da7cc-b367-4196-8a78-9cfeec21f587");
        Assertions.assertNotNull(product);
        assertEquals("1b2da7cc-b367-4196-8a78-9cfeec21f587", product.getId());
        assertEquals("Seguro de Vida", product.getName());
    }

    // @Test
    // public void testGetProductByIdNotFound() {
    //     assertNull(product);
    // }

}

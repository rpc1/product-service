package com.app.productservice.services;

import com.app.productservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {

    Product getProductById(UUID productId);

    Product createProduct(Product product, UUID customer);

    Product updateProduct(Product product);

    Page<Product> getCustomerProducts(UUID customerId, Pageable pageable);

    void deleteProduct(UUID productId);
}

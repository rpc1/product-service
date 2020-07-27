package com.app.productservice.controllers;

import com.app.productservice.dto.ProductDto;
import com.app.productservice.mapper.DataModelMapper;
import com.app.productservice.model.Product;
import com.app.productservice.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
public class ProductController {

    private final DataModelMapper modelMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    public ProductController(DataModelMapper modelMapper, ProductService productService) {
        this.modelMapper = modelMapper;
        this.productService = productService;
    }

    @GetMapping("/products/{id}")
    public Product getProductById(
            @PathVariable(value = "id") UUID id) {
        return productService.getProductById(id);
    }

    @DeleteMapping("/products/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable(value = "id") UUID id) {
        LOGGER.info("Delete product {}", id);
        productService.deleteProduct(id);
    }

    @PutMapping(path = "/products/{productId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Product updateProduct(@RequestBody @Valid ProductDto productDto,
                                 @PathVariable(value = "productId") UUID productId) {
        LOGGER.debug("updating product {} ", productId);
        Product product = modelMapper.map(productDto, Product.class);
        product.setId(productId);
        return productService.updateProduct(product);
    }

    @GetMapping(path = "/customers/{customerId}/products")
    public Page<Product> getProductsForCustomer(@PathVariable(value = "customerId") UUID customerId,
                                                  Pageable pageable) {
        return productService.getCustomerProducts(customerId, pageable);
    }

    @PostMapping(path = "/customers/{customerId}/products", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProductForCustomer(@RequestBody @Valid ProductDto productDto,
                                            @PathVariable(value = "customerId") UUID customerId) {

        LOGGER.debug("New product {} ", productDto.getTitle());
        Product product = modelMapper.map(productDto, Product.class);
        return productService.createProduct(product, customerId);
    }
}

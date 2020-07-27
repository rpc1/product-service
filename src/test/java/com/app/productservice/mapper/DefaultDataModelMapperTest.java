package com.app.productservice.mapper;

import com.app.productservice.dto.CustomerDto;
import com.app.productservice.dto.ProductDto;
import com.app.productservice.model.Customer;
import com.app.productservice.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.math.BigDecimal;
import java.util.UUID;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

class DefaultDataModelMapperTest {

    private static DefaultDataModelMapper modelMapper;

    @BeforeAll
    static void initModelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(PRIVATE);
        modelMapper = new DefaultDataModelMapper(new ModelMapper());
    }

    @Test
    void map_ProductDto_toEntity() {
        ProductDto productDto = new ProductDto(UUID.randomUUID(), "test1", "test2", BigDecimal.valueOf(12.32));
        Product product = modelMapper.map(productDto, Product.class);
        Assertions.assertEquals(productDto.getDescription(), product.getDescription());
        Assertions.assertEquals(productDto.getTitle(), product.getTitle());
        Assertions.assertEquals(productDto.getPrice(), product.getPrice());
        Assertions.assertEquals(productDto.getId(), product.getId());
        Assertions.assertEquals(productDto.isDeleted(), product.isDeleted());
        Assertions.assertNull(product.getCustomer());
        Assertions.assertNull(product.getCreatedAt());
        Assertions.assertNull(product.getModifiedAt());
    }

    @Test
    void map_CustomerDto_toEntity() {
        CustomerDto customerDto = new CustomerDto(UUID.randomUUID(), "test1");
        Customer customer = modelMapper.map(customerDto, Customer.class);
        Assertions.assertEquals(customerDto.getTitle(), customer.getTitle());
        Assertions.assertEquals(customerDto.getId(), customer.getId());
        Assertions.assertEquals(customerDto.isDeleted(), customer.isDeleted());
        Assertions.assertNull(customer.getCreatedAt());
        Assertions.assertNull(customer.getModifiedAt());
    }

}
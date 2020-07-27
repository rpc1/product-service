package com.app.productservice.controllers;

import com.app.productservice.dto.ProductDto;
import com.app.productservice.exceptions.ObjectNotFoundException;
import com.app.productservice.mapper.DataModelMapper;
import com.app.productservice.model.Product;
import com.app.productservice.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    private final String CUSTOMER_ROUTE = "/customers";
    private final String PRODUCT_ROUTE = "/products";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private DataModelMapper dataModelMapper;

    @Test
    void post_createProduct_ShouldCallCreateProduct_and_Return_createdObject() throws Exception {
        String productRequest = "{\n \"deleted\":\"false\",\n\"title\":\"test\"\n }";
        Product product = new Product();
        product.setTitle("Test customer");
        Product createdProduct = new Product();
        createdProduct.setId(UUID.randomUUID());
        UUID customerId = UUID.randomUUID();

        when(dataModelMapper.map(any(ProductDto.class), any())).thenReturn(product);
        when(productService.createProduct(product, customerId)).thenReturn(createdProduct);
        this.mvc.perform(post(CUSTOMER_ROUTE + "/" + customerId + PRODUCT_ROUTE)
                .content(productRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(new ObjectMapper().writeValueAsString(createdProduct)));
        verify(productService).createProduct(product, customerId);
    }

    @Test
    void post_createProductWithWrongPrice_should_return400() throws Exception {
        String productRequest = "{\n \"price\":\"212aaa\",\n\"title\":\"test\"\n }";
              UUID customerId = UUID.randomUUID();

        this.mvc.perform(post(CUSTOMER_ROUTE + "/" + customerId + PRODUCT_ROUTE)
                .content(productRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }
    @Test
    void put_updateProduct_ShouldCallUpdateUpdate_and_Return_updatedObject() throws Exception {
        String updateRequest = "{\"deleted\":\"false\",\"title\":\"test\"\n }";
        Product expectedProduct = new Product();
        expectedProduct.setId(UUID.randomUUID());

        String uuid = UUID.randomUUID().toString();
        expectedProduct.setTitle("Test customer");
        Product updatedProduct = new Product();
        updatedProduct.setTitle("UPDATED PRODUCT");

        ArgumentCaptor<Product> productCaptor =
                ArgumentCaptor.forClass(Product.class);

        when(dataModelMapper.map(any(ProductDto.class), any())).thenReturn(expectedProduct);
        when(productService.updateProduct(expectedProduct)).thenReturn(updatedProduct);
        this.mvc.perform(put(PRODUCT_ROUTE + "/" + uuid)
                .content(updateRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(new ObjectMapper().writeValueAsString(updatedProduct)));
        verify(productService).updateProduct(productCaptor.capture());
        Assertions.assertEquals(uuid, productCaptor.getValue().getId().toString(), "UUID should take from Path");
    }

    @Test
    void getCustomerProducts_should_support_pagination() throws Exception {
        UUID customerId = UUID.randomUUID();
        this.mvc.perform(get(CUSTOMER_ROUTE + "/" + customerId + PRODUCT_ROUTE)
                .param("page", "2")
                .param("size", "20"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(productService).getCustomerProducts(any(), pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();
        Assertions.assertEquals(pageable.getPageNumber(), 2);
        Assertions.assertEquals(pageable.getPageSize(), 20);
    }


    @Test
    void getCustomerProducts_should_call_getCustomerProducts_and_return_page() throws Exception {


        UUID customerId = UUID.randomUUID();

        Product product1 = new Product();
        product1.setId(UUID.randomUUID());
        Product product2 = new Product();
        product2.setId(UUID.randomUUID());


        ArgumentCaptor<UUID> customerIdCaptor =
                ArgumentCaptor.forClass(UUID.class);
        Page<Product> pageProducts = new PageImpl<>(List.of(product1, product2));
        when(productService.getCustomerProducts(customerIdCaptor.capture(), any())).thenReturn(pageProducts);

        this.mvc.perform(get(CUSTOMER_ROUTE + "/" + customerId + PRODUCT_ROUTE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(new ObjectMapper().writeValueAsString(pageProducts)));

        Assertions.assertEquals(customerId, customerIdCaptor.getValue());
    }

    @Test
    void objectNotFoundException_should_return_404() throws Exception {
        when(productService.getProductById(any())).thenThrow(ObjectNotFoundException.class);
        this.mvc.perform(get(PRODUCT_ROUTE + "/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteRequest_should_callDeleteProductService_and_NoContentResponse() throws Exception {
        UUID someUUID = UUID.randomUUID();

        this.mvc.perform(delete(PRODUCT_ROUTE + "/" + someUUID))
                .andExpect(status().isNoContent());
        verify(productService).deleteProduct(someUUID);
    }


    @Test
    void getProductById_should_return_productObject() throws Exception {
        UUID someUUID = UUID.randomUUID();
        Product product = new Product();
        product.setId(someUUID);
        when(productService.getProductById(someUUID)).thenReturn(product);

        this.mvc.perform(get(PRODUCT_ROUTE + "/" + someUUID))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(new ObjectMapper().writeValueAsString(product)));
        verify(productService).getProductById(someUUID);
    }

}
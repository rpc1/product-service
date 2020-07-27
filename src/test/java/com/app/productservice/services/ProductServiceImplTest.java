package com.app.productservice.services;

import com.app.productservice.exceptions.ObjectNotFoundException;
import com.app.productservice.model.Customer;
import com.app.productservice.model.Product;
import com.app.productservice.repositories.CustomerRepository;
import com.app.productservice.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class ProductServiceImplTest {

    private CustomerRepository customerRepository;
    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void init() {
        productRepository = Mockito.mock(ProductRepository.class);
        customerRepository = Mockito.mock(CustomerRepository.class);
        productService = new ProductServiceImpl(productRepository, customerRepository);
    }

    @Test
    void getProductById_shouldCallFindById_and_returnEntity() {
        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        Product returnedProduct = productService.getProductById(productId);
        Assertions.assertEquals(product, returnedProduct);
    }

    @Test
    void getCustomerById_shouldThrowException_if_returnNothing() {
        UUID productId = UUID.randomUUID();
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());
        Assertions.assertThrows(ObjectNotFoundException.class, ()-> productService.getProductById(productId));
    }

    @Test
    void createProduct_shouldFindAndSetCustomer_and_callSave() {
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());
        Product product = new Product();
        product.setId(UUID.randomUUID());

        Mockito.when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        Mockito.when(productRepository.save(product)).thenReturn(product);

        Product returnedProduct = productService.createProduct(product, customer.getId());
        Assertions.assertEquals(product.getCustomer(), customer);
        Assertions.assertEquals(product, returnedProduct);
    }

    @Test
    void createProduct_should_throwObjectNotFound_when_customerNotExists() {
        UUID customerId = UUID.randomUUID();
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        Assertions.assertThrows(ObjectNotFoundException.class, ()-> productService.createProduct(new Product(), customerId));
    }


    @Test
    void updateProduct_getProduct_and_updateFields_and_returnEntity() {
        Product reqProduct = new Product();
        reqProduct.setTitle("test1");
        reqProduct.setDeleted(true);
        reqProduct.setId(UUID.randomUUID());
        reqProduct.setDescription("Test description");
        reqProduct.setPrice(BigDecimal.valueOf(123));

        Product dbProduct = new Product();
        dbProduct.setId(reqProduct.getId());

        Mockito.when(productRepository.findById(reqProduct.getId())).thenReturn(Optional.of(dbProduct));
        Mockito.when(productRepository.save(dbProduct)).thenReturn(dbProduct);

        Product returnedProduct = productService.updateProduct(reqProduct);
        Mockito.verify(productRepository).findById(reqProduct.getId());
        Mockito.verify(productRepository).save(dbProduct);

        Assertions.assertEquals(reqProduct.getTitle(), returnedProduct.getTitle());
        Assertions.assertEquals(reqProduct.isDeleted(), returnedProduct.isDeleted());
        Assertions.assertEquals(reqProduct.getDescription(), returnedProduct.getDescription());
        Assertions.assertEquals(reqProduct.getPrice(), returnedProduct.getPrice());
    }

    @Test
    void getCustomerProducts_should_call_findByCustomerIdWithPageable_and_returnPage() {
        UUID customerId = UUID.randomUUID();

        Pageable pageable = Mockito.mock(Pageable.class);
        Product product1 = new Product();
        product1.setId(UUID.randomUUID());
        Product product2 = new Product();
        product2.setId(UUID.randomUUID());

        Page<Product> pageProducts = new PageImpl<>(List.of(product1, product2));
        Mockito.when(productRepository.findByCustomerId(customerId, pageable)).thenReturn(pageProducts);

        Page<Product> returnedPage = productService.getCustomerProducts(customerId, pageable);
        Assertions.assertEquals(pageProducts, returnedPage);
    }

    @Test
    void deleteProduct_shouldCall_deleteById() {
        UUID productId = UUID.randomUUID();
        productService.deleteProduct(productId);
        Mockito.verify(productRepository).deleteById(productId);
    }
}
package com.app.productservice.services;

import com.app.productservice.exceptions.ObjectNotFoundException;
import com.app.productservice.model.Customer;
import com.app.productservice.model.Product;
import com.app.productservice.repositories.CustomerRepository;
import com.app.productservice.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {


    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public ProductServiceImpl(ProductRepository productRepository, CustomerRepository customerRepository) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Product getProductById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ObjectNotFoundException(productId));
    }

    @Override
    public Product createProduct(Product product, UUID customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new ObjectNotFoundException(customerId));
        product.setCustomer(customer);
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        Product productDb = getProductById(product.getId());
        productDb.setDescription(product.getDescription());
        productDb.setPrice(product.getPrice());
        productDb.setDeleted(product.isDeleted());
        productDb.setTitle(product.getTitle());
        productRepository.save(productDb);
        return productDb;
    }

    @Override
    public Page<Product> getCustomerProducts(UUID customerId, Pageable pageable) {
        return productRepository.findByCustomerId(customerId, pageable);
    }

    @Override
    public void deleteProduct(UUID productId) {
        productRepository.deleteById(productId);
    }
}

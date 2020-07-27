package com.app.productservice.services;

import com.app.productservice.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.UUID;

public interface CustomerService {
    Customer getCustomerById(UUID customerId);

    Customer createCustomer(Customer customer);

    Customer updateCustomer(Customer customer);

    Page<Customer> getAllCustomers(Pageable pageable);

    void deleteCustomer(UUID customerId);

}

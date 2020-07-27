package com.app.productservice.services;

import com.app.productservice.exceptions.ObjectNotFoundException;
import com.app.productservice.model.Customer;
import com.app.productservice.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer getCustomerById(UUID customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ObjectNotFoundException(customerId));
    }

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        Customer customerDb = getCustomerById(customer.getId());
        customerDb.setTitle(customer.getTitle());
        customerDb.setDeleted(customer.isDeleted());
        return customerRepository.save(customerDb);
    }

    @Override
    public Page<Customer> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable);

    }

    @Override
    public void deleteCustomer(UUID customerId) {
        customerRepository.deleteById(customerId);
    }
}

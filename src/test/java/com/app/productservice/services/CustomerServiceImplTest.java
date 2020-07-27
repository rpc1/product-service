package com.app.productservice.services;

import com.app.productservice.exceptions.ObjectNotFoundException;
import com.app.productservice.model.Customer;
import com.app.productservice.repositories.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

class CustomerServiceImplTest {

    private CustomerRepository customerRepository;
    private CustomerService customerService;

    @BeforeEach
    void init() {
        customerRepository = Mockito.mock(CustomerRepository.class);
        customerService = new CustomerServiceImpl(customerRepository);
    }
    
    @Test
    void getCustomerById_shouldCallFindById_and_returnEntity() {
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setId(customerId);
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Customer returnedCustomer = customerService.getCustomerById(customerId);
        Assertions.assertEquals(customer, returnedCustomer);
    }

    @Test
    void getCustomerById_shouldThrowException_if_returnNothing() {
        UUID customerId = UUID.randomUUID();
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        Assertions.assertThrows(ObjectNotFoundException.class, ()-> customerService.getCustomerById(customerId));
    }

    @Test
    void createCustomer_shouldCallSave_and_returnNewCustomer() {
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());
        Mockito.when(customerRepository.save(customer)).thenReturn(customer);

        Customer returnedCustomer = customerService.createCustomer(customer);
        Assertions.assertEquals(customer, returnedCustomer);
    }

    @Test
    void updateCustomer_should_getCustomer_and_updateTitleAndDeleted_and_returnEntity() {
        Customer reqCustomer = new Customer();
        reqCustomer.setTitle("test1");
        reqCustomer.setDeleted(true);
        reqCustomer.setId(UUID.randomUUID());

        Customer dbCustomer = new Customer();
        dbCustomer.setId(reqCustomer.getId());

        Mockito.when(customerRepository.findById(reqCustomer.getId())).thenReturn(Optional.of(dbCustomer));
        Mockito.when(customerRepository.save(dbCustomer)).thenReturn(dbCustomer);

        Customer returnedCustomer = customerService.updateCustomer(reqCustomer);
        Mockito.verify(customerRepository).findById(reqCustomer.getId());
        Mockito.verify(customerRepository).save(dbCustomer);

        Assertions.assertEquals(reqCustomer.getTitle(), returnedCustomer.getTitle());
        Assertions.assertEquals(reqCustomer.isDeleted(), returnedCustomer.isDeleted());
    }

    @Test
    void getAllCustomers_should_callFindAllWithPageable_and_returnPage() {
        Pageable pageable = Mockito.mock(Pageable.class);
        Customer customer1 = new Customer();
        customer1.setId(UUID.randomUUID());
        Customer customer2 = new Customer();
        customer2.setId(UUID.randomUUID());

        Page<Customer> pageCustomers = new PageImpl<>(List.of(customer1, customer2));
        Mockito.when(customerRepository.findAll(pageable)).thenReturn(pageCustomers);

        Page<Customer> returnedPage = customerService.getAllCustomers(pageable);
        Assertions.assertEquals(pageCustomers, returnedPage);
    }

    @Test
    void deleteCustomer_should_Call_deleteById() {
        UUID customerId = UUID.randomUUID();
        customerService.deleteCustomer(customerId);
        Mockito.verify(customerRepository).deleteById(customerId);
    }
}
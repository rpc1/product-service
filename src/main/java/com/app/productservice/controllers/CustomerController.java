package com.app.productservice.controllers;

import com.app.productservice.dto.CustomerDto;
import com.app.productservice.mapper.DataModelMapper;
import com.app.productservice.model.Customer;
import com.app.productservice.services.CustomerService;
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
@RequestMapping("/customers")
public class CustomerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerService customerService;
    private final DataModelMapper modelMapper;

    public CustomerController(CustomerService customerService, DataModelMapper modelMapper) {

        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public Page<Customer> getCustomers(Pageable pageable) {
        return customerService.getAllCustomers(pageable);
    }

    @GetMapping("{customerId}")
    public Customer getCustomerById(
            @PathVariable(value = "customerId") UUID id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Customer createCustomer(@RequestBody @Valid CustomerDto customerDto) {
        LOGGER.debug("New customer {} ", customerDto.getTitle());
        Customer customer = modelMapper.map(customerDto, Customer.class);
        return customerService.createCustomer(customer);
    }

    @PutMapping(path = "{customerId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Customer updateCustomer(@RequestBody @Valid CustomerDto customerDto,
                                   @PathVariable(value = "customerId") UUID id) {
        LOGGER.debug("update request for customer {} ", id);
        Customer customer = modelMapper.map(customerDto, Customer.class);
        customer.setId(id);
        return customerService.updateCustomer(customer);
    }

    @DeleteMapping("{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable(value = "customerId") UUID id) {
        LOGGER.info("Delete customer {}", id);
        customerService.deleteCustomer(id);
    }
}

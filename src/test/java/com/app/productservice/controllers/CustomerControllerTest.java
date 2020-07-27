package com.app.productservice.controllers;

import com.app.productservice.dto.CustomerDto;
import com.app.productservice.exceptions.ObjectNotFoundException;
import com.app.productservice.mapper.DataModelMapper;
import com.app.productservice.model.Customer;
import com.app.productservice.services.CustomerService;
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
@WebMvcTest(controllers = CustomerController.class)
class CustomerControllerTest {

    private final String CUSTOMER_ROUTE = "/customers";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private DataModelMapper dataModelMapper;

    @Test
    void post_createCustomer_ShouldCallCreateCustomer_and_Return_createdObject() throws Exception {
        String customerRequest = "{\n" +
                "    \"deleted\":\"false\",\n" +
                "    \"title\":\"test\"\n" +
                "}";
        Customer expectedCustomer = new Customer();
        expectedCustomer.setTitle("Test customer");

        Customer createdCustomer = new Customer();
        createdCustomer.setId(UUID.randomUUID());

        when(dataModelMapper.map(any(CustomerDto.class), any())).thenReturn(expectedCustomer);
        when(customerService.createCustomer(expectedCustomer)).thenReturn(createdCustomer);
        this.mvc.perform(post(CUSTOMER_ROUTE)
                .content(customerRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(new ObjectMapper().writeValueAsString(createdCustomer)));
        verify(customerService).createCustomer(expectedCustomer);
    }

    @Test
    void put_updateCustomer_ShouldCallUpdateCustomer_and_Return_updatedObject() throws Exception {
        String customerRequest = "{\"deleted\":\"false\",\"title\":\"test\"\n }";
        Customer expectedCustomer = new Customer();
        expectedCustomer.setId(UUID.randomUUID());

        String uuid = UUID.randomUUID().toString();

        expectedCustomer.setTitle("Test customer");
        Customer updatedCustomer = new Customer();

        ArgumentCaptor<Customer> customerCaptor =
                ArgumentCaptor.forClass(Customer.class);

        when(dataModelMapper.map(any(CustomerDto.class), any())).thenReturn(expectedCustomer);
        when(customerService.updateCustomer(expectedCustomer)).thenReturn(updatedCustomer);
        this.mvc.perform(put(CUSTOMER_ROUTE + "/" + uuid)
                .content(customerRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(new ObjectMapper().writeValueAsString(updatedCustomer)));
        verify(customerService).updateCustomer(customerCaptor.capture());
        Assertions.assertEquals(uuid, customerCaptor.getValue().getId().toString(), "UUID should take from Path");
    }

    @Test
    void getCustomers_should_support_pagination() throws Exception {
        this.mvc.perform(get(CUSTOMER_ROUTE)
                .param("page", "2")
                .param("size", "20"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(customerService).getAllCustomers(pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();

        Assertions.assertEquals(pageable.getPageNumber(), 2);
        Assertions.assertEquals(pageable.getPageSize(), 20);

    }


    @Test
    void getCustomers_should_call_getAllCustomers_and_return_page() throws Exception {

        Customer customer1 = new Customer();
        customer1.setId(UUID.randomUUID());
        Customer customer2 = new Customer();
        customer2.setId(UUID.randomUUID());


        Page<Customer> pageCustomers = new PageImpl<>(List.of(customer1, customer2));
        when(customerService.getAllCustomers(any())).thenReturn(pageCustomers);

        this.mvc.perform(get(CUSTOMER_ROUTE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(new ObjectMapper().writeValueAsString(pageCustomers)))
        ;
    }

    @Test
    void objectNotFoundException_should_return_404() throws Exception {
        when(customerService.getCustomerById(any())).thenThrow(ObjectNotFoundException.class);
        this.mvc.perform(get(CUSTOMER_ROUTE + "/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteRequest_should_callDeleteCustomerService_and_NoContentResponse() throws Exception {
        UUID someUUID = UUID.randomUUID();

        this.mvc.perform(delete(CUSTOMER_ROUTE + "/" + someUUID))
                .andExpect(status().isNoContent());
        verify(customerService).deleteCustomer(someUUID);
    }

    @Test
    void deleteRequest_without_UUID_should_return_405() throws Exception {
        UUID someUUID = UUID.randomUUID();

        this.mvc.perform(delete(CUSTOMER_ROUTE + "/"))
                .andExpect(status().isMethodNotAllowed());
        verify(customerService, never()).deleteCustomer(someUUID);
    }

    @Test
    void getCustomerById_should_return_customerObject() throws Exception {
        UUID someUUID = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setId(someUUID);
        when(customerService.getCustomerById(someUUID)).thenReturn(customer);

        this.mvc.perform(get(CUSTOMER_ROUTE + "/" + someUUID))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(new ObjectMapper().writeValueAsString(customer)));
        verify(customerService).getCustomerById(someUUID);
    }
}
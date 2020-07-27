package com.app.productservice.model;

import com.app.productservice.AmountDeserializer;
import com.app.productservice.AmountSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Access(AccessType.FIELD)
public class Product extends AbstractDataObject {

    private String description;
    @JsonDeserialize(using = AmountDeserializer.class)
    @JsonSerialize(using = AmountSerializer.class)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, targetEntity = Customer.class)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}


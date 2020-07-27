package com.app.productservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "customers")
@Access(AccessType.FIELD)
public class Customer extends AbstractDataObject {

    @OneToMany(targetEntity = Product.class, mappedBy = "customer", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Product> products;

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}

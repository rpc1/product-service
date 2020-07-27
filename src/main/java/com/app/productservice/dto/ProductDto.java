package com.app.productservice.dto;

import com.app.productservice.AmountDeserializer;
import com.app.productservice.AmountSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

public class ProductDto extends AbstractDtoObject {

    @Size(max=1024)
    private String description;

    @JsonDeserialize(using = AmountDeserializer.class)
    @JsonSerialize(using = AmountSerializer.class)
    private BigDecimal price;

    public ProductDto(UUID id, String title, String description, BigDecimal price) {
        super(id, title);
        this.description = description;
        this.price = price;
    }

    public ProductDto() {

    }

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
}

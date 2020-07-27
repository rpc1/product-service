package com.app.productservice;

import com.app.productservice.dto.ProductDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class AmountSerializerTest {

    @Test
    void  serializeWithFraction4Digits_should_roundTo2Digits() throws JsonProcessingException {
        String json = "{\n" +
                "    \"deleted\":\"false\",\n" +
                "    \"title\":\"product1\",\n" +
                "    \"description\": \"desce\",\n" +
                "    \"price\": 123.1234\n" +
                "\n" +
                "}";

        ProductDto product = new ObjectMapper().readValue(json, ProductDto.class);
        Assertions.assertEquals(BigDecimal.valueOf(123.12), product.getPrice());
    }

    @Test
    void  serializeWithFraction4Digits_should_roundUPTo2Digits() throws JsonProcessingException {
        String json = "{\n" +
                "    \"deleted\":\"false\",\n" +
                "    \"title\":\"product1\",\n" +
                "    \"description\": \"desce\",\n" +
                "    \"price\": 123.1254\n" +
                "\n" +
                "}";

        ProductDto product = new ObjectMapper().readValue(json, ProductDto.class);
        Assertions.assertEquals(BigDecimal.valueOf(123.13), product.getPrice());
    }
}
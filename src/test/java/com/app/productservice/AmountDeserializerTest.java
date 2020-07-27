package com.app.productservice;

import com.app.productservice.dto.ProductDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class AmountDeserializerTest {

    @Test
    void price_shouldDeserializeTo2DigitsFraction() throws JsonProcessingException {
        ProductDto productDto = new ProductDto();
        productDto.setPrice(BigDecimal.valueOf(123.1234));
        String json = new ObjectMapper().writeValueAsString(productDto);
        Assertions.assertTrue(json.contains("\"price\":\"123.12\""));
    }

    @Test
    void price_shouldDeserializeAndRoundUpTo2DigitsFraction() throws JsonProcessingException {
        ProductDto productDto = new ProductDto();
        productDto.setPrice(BigDecimal.valueOf(123.4567));
        String json = new ObjectMapper().writeValueAsString(productDto);
        Assertions.assertTrue(json.contains("\"price\":\"123.46\""), json);
    }


}
package com.app.productservice;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class AmountDeserializer extends JsonDeserializer<BigDecimal> {

    @Override
    public BigDecimal deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return jp.getDecimalValue().setScale(2, RoundingMode.HALF_UP);
    }
}
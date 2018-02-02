package com.example.wallet.api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

public class CurrencyDeserializer extends JsonDeserializer<Currency> {

    @Override
    public Currency deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        JsonNode node = p.readValueAsTree();

        JsonNode numberDecimal = Optional.ofNullable(node.get("amount").get("$numberDecimal")).orElse(node.get("amount"));

        //because for some reason "number decimal" has quotes...
        BigDecimal amount = new BigDecimal(numberDecimal.asText().replace("\"", ""));

        String type = node.get("type").asText();

        return new Currency(type, amount);
    }
}

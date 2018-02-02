package com.example.wallet.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import lombok.Value;

import java.math.BigDecimal;


@Value
@JsonIgnoreProperties(value = { "_id" })
public class Currency{

    public final String type;
    public final BigDecimal amount;

    @JsonCreator
    public Currency(@JsonProperty("type")String type, @JsonProperty("amount")BigDecimal amount) {
        this.type = Preconditions.checkNotNull(type, "type cannot be empty");
        this.amount = Preconditions.checkNotNull(amount, "type cannot be empty");

    }
}

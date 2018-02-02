package com.example.magicmoneymint.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import lombok.Value;

import java.math.BigDecimal;

@Value
@JsonDeserialize
public class MintMessage {

    public final String currency;
    public final BigDecimal amount;

    @JsonCreator
    public MintMessage(String currency, BigDecimal amount) {
        this.currency = Preconditions.checkNotNull(currency, "currency cannot be empty");
        this.amount = Preconditions.checkNotNull(amount, "amount cannot be empty");
    }
}

package com.example.magicmoneymint.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.serialization.CompressedJsonable;
import lombok.Value;

import java.math.BigDecimal;

@SuppressWarnings("serial")
@Value
@JsonDeserialize
public final class MagicMintState implements CompressedJsonable {

    public final BigDecimal totalValue;
    public final String currency;
    public final String timestamp;

    @JsonCreator
    public MagicMintState(BigDecimal totalValue, String currency, String timestamp) {
        this.totalValue = Preconditions.checkNotNull(totalValue, "totalValue cannot be empty");
        this.currency = Preconditions.checkNotNull(currency, "currency cannot be empty");
        this.timestamp = Preconditions.checkNotNull(timestamp, "timestamp cannot be empty");
    }
}

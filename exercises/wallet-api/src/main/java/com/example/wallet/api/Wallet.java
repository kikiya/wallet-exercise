package com.example.wallet.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Value;

import java.util.List;

@Value
@JsonDeserialize
@JsonIgnoreProperties(value = {"_id"})
public class Wallet {

    public final String owner;
    public final List<Currency> balances;

    @JsonCreator
    public Wallet(@JsonProperty("owner") String owner, @JsonProperty("balances") List<Currency> balances) {
        this.owner = owner;
        this.balances = balances;
    }


}

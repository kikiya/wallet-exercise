/*
 * 
 */
package com.example.wallet.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.lightbend.lagom.serialization.CompressedJsonable;
import lombok.Value;

import java.math.BigDecimal;

/**
 * The state for the {@link WalletEntity} entity.
 */
@SuppressWarnings("serial")
@Value
@JsonDeserialize
public final class WalletState implements CompressedJsonable {

  public final ImmutableMap<String, BigDecimal> balances;
  public final String timestamp;


  @JsonCreator
  public WalletState(ImmutableMap<String, BigDecimal> balances, String timestamp) {
    this.balances = Preconditions.checkNotNull(balances, "balances cannot be empty");
    this.timestamp = Preconditions.checkNotNull(timestamp, "timestamp cannot be empty");
  }
}

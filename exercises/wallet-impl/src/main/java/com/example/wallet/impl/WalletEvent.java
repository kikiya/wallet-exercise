/*
 * 
 */
package com.example.wallet.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventShards;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;

import java.math.BigDecimal;

/**
 * This interface defines all the events that the WalletEntity supports.
 * <p>
 * By convention, the events should be inner classes of the interface, which
 * makes it simple to get a complete picture of what events an entity has.
 */
public interface WalletEvent extends Jsonable, AggregateEvent<WalletEvent> {

  /**
   * Tags are used for getting and publishing streams of events. Each event
   * will have this tag, and in this case, we are partitioning the tags into
   * 4 shards, which means we can have 4 concurrent processors/publishers of
   * events.
   */
   AggregateEventShards<WalletEvent> TAG = AggregateEventTag.sharded(WalletEvent.class, 4);

  /**
   * An event that represents a change in greeting message.
   */
  @SuppressWarnings("serial")
  @Value
  @JsonDeserialize
  final class GreetingMessageChanged implements WalletEvent {
	public final String name;
    public final String message;

    @JsonCreator
    public GreetingMessageChanged(String name, String message) {
      this.name = Preconditions.checkNotNull(name, "name");
      this.message = Preconditions.checkNotNull(message, "message");
    }
  }

  @SuppressWarnings("serial")
  @Value
  @JsonDeserialize
  final class CurrencyDeposited implements WalletEvent {
    public final String id;
    public final String currency;
    public final BigDecimal amount;

    @JsonCreator
    public CurrencyDeposited(String id, String currency, BigDecimal amount) {
      this.id = Preconditions.checkNotNull(id, "receivingWalletId");
      this.currency = Preconditions.checkNotNull(currency, "currency cannot be null");
      this.amount =  Preconditions.checkNotNull(amount, "amount cannot be empty");

    }
  }

  @SuppressWarnings("serial")
  @Value
  @JsonDeserialize
  final class NewWalletCreated implements WalletEvent {
    public final String id;
    public final String currency;
    public final BigDecimal amount;

    @JsonCreator
    public NewWalletCreated(String id, String currency, BigDecimal amount) {
      this.id = Preconditions.checkNotNull(id, "receivingWalletId");
      this.currency = Preconditions.checkNotNull(currency, "currency cannot be null");
      this.amount =  Preconditions.checkNotNull(amount, "amount cannot be empty");

    }
  }

  @SuppressWarnings("serial")
  @Value
  @JsonDeserialize
  final class CurrencySent implements WalletEvent {
    public final String receivingWalletId;
    public final String sendingWalletId;
    public final String currency;
    public final BigDecimal amount;

    @JsonCreator
    public CurrencySent(String receivingWalletId, String currency, BigDecimal amount, String sendingWalletId) {
      this.receivingWalletId = Preconditions.checkNotNull(receivingWalletId, "receivingWalletId cannot be empty");
      this.currency = Preconditions.checkNotNull(currency, "currency cannot be null");
      this.amount =  Preconditions.checkNotNull(amount, "amount cannot be empty");
      this.sendingWalletId =  Preconditions.checkNotNull(sendingWalletId, "sendingWalletId cannot be empty");
    }
  }

  @Override
  default AggregateEventTagger<WalletEvent> aggregateTag() {
    return TAG;
  }
}

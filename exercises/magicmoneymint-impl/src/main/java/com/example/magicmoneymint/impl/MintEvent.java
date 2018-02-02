/*
 * 
 */
package com.example.magicmoneymint.impl;

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
 * This interface defines all the events that the MintEntity supports.
 * <p>
 * By convention, the events should be inner classes of the interface, which
 * makes it simple to get a complete picture of what events an entity has.
 */
public interface MintEvent extends Jsonable, AggregateEvent<MintEvent> {

  /**
   * Tags are used for getting and publishing streams of events. Each event
   * will have this tag, and in this case, we are partitioning the tags into
   * 4 shards, which means we can have 4 concurrent processors/publishers of
   * events.
   */
  AggregateEventShards<MintEvent> TAG = AggregateEventTag.sharded(MintEvent.class, 4);

  @SuppressWarnings("serial")
  @Value
  @JsonDeserialize
  final class MadeMoney implements MintEvent {
    public final String currency;
    public final BigDecimal amount;

    @JsonCreator
    public MadeMoney(String currency, BigDecimal amount) {
      this.currency = Preconditions.checkNotNull(currency, "currency");
      this.amount = Preconditions.checkNotNull(amount, "amount");
    }
  }

  @Override
  default AggregateEventTagger<MintEvent> aggregateTag() {
    return TAG;
  }
}

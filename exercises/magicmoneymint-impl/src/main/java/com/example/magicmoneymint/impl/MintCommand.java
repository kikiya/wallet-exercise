/*
 * 
 */
package com.example.magicmoneymint.impl;

import akka.Done;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;

import java.math.BigDecimal;

/**
 * This interface defines all the commands that the MintEntity supports.
 * 
 * By convention, the commands should be inner classes of the interface, which
 * makes it simple to get a complete picture of what commands an entity
 * supports.
 */
public interface MintCommand extends Jsonable {

  /**
   * A command to switch the greeting message.
   * <p>
   * It has a reply type of {@link akka.Done}, which is sent back to the caller
   * when all the events emitted by this command are successfully persisted.
   */
  @SuppressWarnings("serial")
  @Value
  @JsonDeserialize
  final class UseGreetingMessage implements MintCommand, CompressedJsonable, PersistentEntity.ReplyType<Done> {
    public final String message;

    @JsonCreator
    public UseGreetingMessage(String message) {
      this.message = Preconditions.checkNotNull(message, "message");
    }
  }

  /**
   * A command to say checkBalance to someone using the current greeting message.
   * <p>
   * The reply type is String, and will contain the message to say to that
   * person.
   */
  @SuppressWarnings("serial")
  @Value
  @JsonDeserialize
  final class MintBalance implements MintCommand, PersistentEntity.ReplyType<String> {
    public final String name;

    @JsonCreator
    public MintBalance(String name) {
      this.name = Preconditions.checkNotNull(name, "currency");
    }
  }

  @SuppressWarnings("serial")
  @Value
  @JsonDeserialize
  final class MakeMoney implements MintCommand, PersistentEntity.ReplyType<Done> {

    public final String currency;
    public final BigDecimal amount;

    @JsonCreator
    public MakeMoney(String currency, BigDecimal amount) {
      this.currency = Preconditions.checkNotNull(currency, "currency cannot be empty");;
      this.amount = Preconditions.checkNotNull(amount, "amount cannot be empty");
    }
  }

  @SuppressWarnings("serial")
  @Value
  @JsonDeserialize
  final class TakeMoney implements MintCommand, PersistentEntity.ReplyType<Done> {

    public final String currency;
    public final BigDecimal amount;

    @JsonCreator
    public TakeMoney(String currency, BigDecimal amount) {
      this.currency = Preconditions.checkNotNull(currency, "currency cannot be empty");;
      this.amount = Preconditions.checkNotNull(amount, "amount cannot be empty");
    }
  }

}

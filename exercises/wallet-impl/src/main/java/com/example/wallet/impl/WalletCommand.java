/*
 * 
 */
package com.example.wallet.impl;

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
 * This interface defines all the commands that the WalletEntity supports.
 * 
 * By convention, the commands should be inner classes of the interface, which
 * makes it simple to get a complete picture of what commands an entity
 * supports.
 */
public interface WalletCommand extends Jsonable {

  /**
   * A command to switch the greeting message.
   * <p>
   * It has a reply type of {@link akka.Done}, which is sent back to the caller
   * when all the events emitted by this command are successfully persisted.
   */
  @SuppressWarnings("serial")
  @Value
  @JsonDeserialize
  final class UseGreetingMessage implements WalletCommand, CompressedJsonable, PersistentEntity.ReplyType<Done> {
    public final String message;

    @JsonCreator
    public UseGreetingMessage(String message) {
      this.message = Preconditions.checkNotNull(message, "message");
    }
  }

  /**
   * A command to say balance to someone using the current greeting message.
   * <p>
   * The reply type is String, and will contain the message to say to that
   * person.
   */
  @SuppressWarnings("serial")
  @Value
  @JsonDeserialize
  final class Hello implements WalletCommand, PersistentEntity.ReplyType<String> {
    public final String name;

    @JsonCreator
    public Hello(String name) {
      this.name = Preconditions.checkNotNull(name, "name");
    }
  }

  @SuppressWarnings("serial")
  @Value
  @JsonDeserialize
  final class Deposit implements WalletCommand, PersistentEntity.ReplyType<Done> {
    public final String cryptocurrency;
    public final BigDecimal amount;

    @JsonCreator
    public Deposit(String cryptocurrency, BigDecimal amount) {
      this.cryptocurrency = Preconditions.checkNotNull(cryptocurrency, "currency cannot be null");
      this.amount =  Preconditions.checkNotNull(amount, "amount cannot be empty");

    }
  }

    @SuppressWarnings("serial")
    @Value
    @JsonDeserialize
    final class Withdraw implements WalletCommand, PersistentEntity.ReplyType<String> {
        public final String cryptocurrency;
        public final BigDecimal amount;

        @JsonCreator
        public Withdraw(String cryptocurrency, BigDecimal amount) {
            this.cryptocurrency = Preconditions.checkNotNull(cryptocurrency, "currency cannot be null");
            this.amount =  Preconditions.checkNotNull(amount, "amount cannot be empty");

        }
    }

    @SuppressWarnings("serial")
    @Value
    @JsonDeserialize
    final class Send implements WalletCommand, PersistentEntity.ReplyType<Done> {
        public final String cryptocurrency;
        public final BigDecimal amount;
        public final String receivingWalletID;

        @JsonCreator
        public Send(String cryptocurrency, BigDecimal amount, String receivingWalletID) {
            this.cryptocurrency = Preconditions.checkNotNull(cryptocurrency, "currency cannot be null");
            this.amount =  Preconditions.checkNotNull(amount, "amount cannot be empty");
            this.receivingWalletID =  Preconditions.checkNotNull(receivingWalletID, "receivingWalletID cannot be empty");

        }
    }

}

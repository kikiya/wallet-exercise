/*
 * 
 */
package com.example.wallet.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Preconditions;
import lombok.Value;

import java.math.BigDecimal;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = WalletEventPublish.CurrencyDeposited.class, name = "crypto-deposited"),
        @JsonSubTypes.Type(value = WalletEventPublish.CurrencySent.class, name = "crypto-sent")
})
public interface WalletEventPublish {

  String getReceivingWalletId();

  @Value
  final class CurrencyDeposited implements WalletEventPublish {
    public final String receivingWalletId;
    public final String cryptocurrency;
    public final BigDecimal amount;

    @JsonCreator
    public CurrencyDeposited(String receivingWalletId, String cryptocurrency, BigDecimal amount) {
      this.receivingWalletId = Preconditions.checkNotNull(receivingWalletId, "receivingWalletId");
      this.cryptocurrency = Preconditions.checkNotNull(cryptocurrency, "cryptocurrency cannot be null");
      this.amount =  Preconditions.checkNotNull(amount, "amount cannot be empty");

    }
  }

  @Value
  final class CurrencySent implements WalletEventPublish {
    public final String receivingWalletId;
    public final String sendingWalletId;
    public final String cryptocurrency;
    public final BigDecimal amount;

    @JsonCreator
    public CurrencySent(String receivingWalletId, String cryptocurrency, BigDecimal amount, String sendingWalletId) {
      this.receivingWalletId = Preconditions.checkNotNull(receivingWalletId, "receivingWalletId");
      this.cryptocurrency = Preconditions.checkNotNull(cryptocurrency, "cryptocurrency cannot be null");
      this.amount =  Preconditions.checkNotNull(amount, "amount cannot be empty");
      this.sendingWalletId =  Preconditions.checkNotNull(sendingWalletId, "sendingWalletId cannot be empty");

    }
  }

}

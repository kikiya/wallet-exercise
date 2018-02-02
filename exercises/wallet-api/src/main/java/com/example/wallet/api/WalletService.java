/*
 * 
 */
package com.example.wallet.api;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;

/**
 * The wallet service interface.
 * <p>
 * This describes everything that Lagom needs to know about how to serve and
 * consume the WalletService.
 */
public interface WalletService extends Service {

  /**
   * Example: curl http://localhost:9000/api/balance/Alice
   */
  ServiceCall<NotUsed, String> balance(String walletId);

  @Override
  default Descriptor descriptor() {
    // @formatter:off
    return named("wallet").withCalls(
            pathCall("/api/balance/:walletId", this::balance)
      ).withAutoAcl(true);
    // @formatter:on
  }
}

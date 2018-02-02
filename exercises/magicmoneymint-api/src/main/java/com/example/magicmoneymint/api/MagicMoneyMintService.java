/*
 * 
 */
package com.example.magicmoneymint.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;

/**
 * The magicmoneymint service interface.
 * <p>
 * This describes everything that Lagom needs to know about how to serve and
 * consume the MagicMoneyMintService.
 */
public interface MagicMoneyMintService extends Service {

  ServiceCall<NotUsed, String> checkBalance(String id);

  ServiceCall<MintMessage, Done> magicMoney();

  @Override
  default Descriptor descriptor() {
    // @formatter:off
    return named("magicmoneymint").withCalls(
            pathCall("/api/check-balance/:id",  this::checkBalance),
            pathCall("/api/magic-money/mint", this::magicMoney)
      ).withAutoAcl(true);
    // @formatter:on
  }
}

/*
 * 
 */
package com.example.wallet.impl;

import akka.NotUsed;
import com.example.wallet.api.WalletService;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * Implementation of the WalletService.
 */
public class WalletServiceImpl implements WalletService {

  @Override
  public ServiceCall<NotUsed, String> balance(String id) {
    return request -> completedFuture(String.format("Where's %s's money!?", id));
  }

}

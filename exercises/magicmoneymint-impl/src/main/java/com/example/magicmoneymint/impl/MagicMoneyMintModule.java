/*
 * 
 */
package com.example.magicmoneymint.impl;

import com.example.magicmoneymint.api.MagicMoneyMintService;
import com.example.wallet.api.WalletService;
import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

/**
 * The module that binds the MagicMoneyMintService so that it can be served.
 */
public class MagicMoneyMintModule extends AbstractModule implements ServiceGuiceSupport {
  @Override
  protected void configure() {
    bindService(MagicMoneyMintService.class, MagicMoneyMintServiceImpl.class);

    bindClient(WalletService.class);

    // Bind the subscriber eagerly to ensure it starts up
    bind(WalletStreamSubscriber.class).asEagerSingleton();
  }
}

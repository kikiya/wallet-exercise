/*
 * 
 */
package com.example.wallet.impl;

import com.example.wallet.api.WalletService;
import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

/**
 * The module that binds the WalletService so that it can be served.
 */
public class WalletModule extends AbstractModule implements ServiceGuiceSupport {
  @Override
  protected void configure() {
    bindService(WalletService.class, WalletServiceImpl.class);
  }

}

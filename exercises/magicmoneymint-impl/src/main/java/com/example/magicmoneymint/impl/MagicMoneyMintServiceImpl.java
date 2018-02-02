/*
 * 
 */
package com.example.magicmoneymint.impl;

import akka.Done;
import akka.NotUsed;
import com.example.magicmoneymint.api.MagicMoneyMintService;
import com.example.magicmoneymint.api.MintMessage;
import com.example.magicmoneymint.impl.MintCommand.MakeMoney;
import com.example.magicmoneymint.impl.MintCommand.MintBalance;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;

import javax.inject.Inject;

/**
 * Implementation of the MagicMoneyMintService.
 */
public class MagicMoneyMintServiceImpl implements MagicMoneyMintService {

  private final PersistentEntityRegistry persistentEntityRegistry;

  @Inject
  public MagicMoneyMintServiceImpl(PersistentEntityRegistry persistentEntityRegistry) {
    this.persistentEntityRegistry = persistentEntityRegistry;
    persistentEntityRegistry.register(MintEntity.class);
  }

  @Override
  public ServiceCall<NotUsed, String> checkBalance(String id) {
    return request -> {
      // Look up the checkBalance world entity for the given ID.
      PersistentEntityRef<MintCommand> ref = persistentEntityRegistry.refFor(MintEntity.class, id);
      // Ask the entity the MintBalance command.
      return ref.ask(new MintBalance(id));
    };
  }

  @Override
  public ServiceCall<MintMessage, Done> magicMoney() {
    return request -> {
      PersistentEntityRef<MintCommand> ref = persistentEntityRegistry.refFor(MintEntity.class, request.currency);

      return ref.ask(new MakeMoney(request.currency, request.amount)) ;
    };
  }

}

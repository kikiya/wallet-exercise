package com.example.wallet.impl;

import akka.Done;
import akka.japi.Pair;
import akka.stream.javadsl.Flow;
import com.example.wallet.api.Currency;
import com.example.wallet.api.Wallet;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;
import com.lightbend.lagom.javadsl.persistence.*;
import org.pcollections.PSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class WalletEventProcessor extends ReadSideProcessor<WalletEvent> {
    private final Logger log = LoggerFactory.getLogger(WalletEventProcessor.class);


    private final PersistentEntityRegistry persistentEntityRegistry;
    private final WalletRepository walletRepository;

    @Inject
    public WalletEventProcessor(PersistentEntityRegistry persistentEntityRegistry, WalletRepository walletRepository) {
        this.persistentEntityRegistry = persistentEntityRegistry;
        persistentEntityRegistry.register(WalletEntity.class);

        this.walletRepository = walletRepository;

    }

    private CompletionStage<Done> processCurrencyDeposited(WalletEvent.CurrencyDeposited event) throws JsonProcessingException{
        Currency currency = new Currency(event.currency, event.amount);

        ImmutableList.Builder<Currency> list = ImmutableList.builder();

        return walletRepository.addCurrency(new Wallet(event.id, list.add(currency).build().asList()), currency);
    }

    private CompletionStage<Done> processNewWallet(WalletEvent.NewWalletCreated event) throws JsonProcessingException{

        Currency currency = new Currency(event.currency, event.amount);

        ImmutableList.Builder<Currency> list = ImmutableList.builder();

        return walletRepository.create(new Wallet(event.id, list.add(currency).build().asList()));
    }

    @Override
    public ReadSideHandler<WalletEvent> buildHandler() {

        return new ReadSideHandler<WalletEvent>() {

            @Override
            public Flow<Pair<WalletEvent, Offset>, Done, ?> handle() {
                return Flow.<Pair<WalletEvent, Offset>>create()
                        .mapAsync(1, eventAndOffset ->{

                                    if (eventAndOffset.first() instanceof WalletEvent.CurrencySent) {
                                        WalletEvent.CurrencySent currencySent = (WalletEvent.CurrencySent) eventAndOffset.first();
                                        log.info("********************** EventProcessor Currency Sent event: "+currencySent);

                                        PersistentEntityRef entityRef = persistentEntityRegistry.refFor(WalletEntity.class, currencySent.receivingWalletId);
                                        return entityRef.ask(new WalletCommand.Deposit(currencySent.currency, currencySent.amount));
                                    } else if (eventAndOffset.first() instanceof WalletEvent.CurrencyDeposited) {
                                        WalletEvent.CurrencyDeposited currencyDeposited = (WalletEvent.CurrencyDeposited) eventAndOffset.first();
                                        log.info("********************** EventProcessor Currency Deposited Event: "+currencyDeposited);

                                        return processCurrencyDeposited(currencyDeposited);
                                    } else if (eventAndOffset.first() instanceof WalletEvent.NewWalletCreated) {
                                        WalletEvent.NewWalletCreated newWalletCreated = (WalletEvent.NewWalletCreated) eventAndOffset.first();
                                        log.info("********************** EventProcessor NEW WALLET event: "+newWalletCreated);

                                        return processNewWallet(newWalletCreated);
                                    } else
                                        return CompletableFuture.completedFuture(Done.getInstance());
                                }
                        );
            }

        };
    }

    @Override
    public PSequence<AggregateEventTag<WalletEvent>> aggregateTags() {
        return WalletEvent.TAG.allTags();
    }
}

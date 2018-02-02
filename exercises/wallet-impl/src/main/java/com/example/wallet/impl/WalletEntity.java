/*
 * 
 */
package com.example.wallet.impl;

import akka.Done;
import com.example.wallet.impl.WalletCommand.Deposit;
import com.example.wallet.impl.WalletCommand.Hello;
import com.google.common.collect.ImmutableMap;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;


public class WalletEntity extends PersistentEntity<WalletCommand, WalletEvent, WalletState> {

  /**
   * An entity can define different behaviours for different states, but it will
   * always start with an initial behaviour. This entity only has one behaviour.
   */
  @Override
  public Behavior initialBehavior(Optional<WalletState> snapshotState) {


    /*
     * Behaviour is defined using a behaviour builder. The behaviour builder
     * starts with a state, if this entity supports snapshotting (an
     * optimisation that allows the state itself to be persisted to combine many
     * events into one), then the passed in snapshotState may have a value that
     * can be used.
     *
     * Otherwise, the default state is to use the Hello greeting.
     */
    //default balances
    ImmutableMap<String, BigDecimal> defaultBalanaces = ImmutableMap.<String, BigDecimal>builder().build();
    BehaviorBuilder b = newBehaviorBuilder(
        snapshotState.orElse(new WalletState(defaultBalanaces, LocalDateTime.now().toString())));

    /*
     * Command handler for the UseGreetingMessage command.
     */
    b.setCommandHandler(Deposit.class, (cmd, ctx) -> {


                if (state().balances.isEmpty()) {

                    return ctx.thenPersist(new WalletEvent.NewWalletCreated(entityId(), cmd.cryptocurrency, cmd.amount),
                            evt -> ctx.reply(Done.getInstance()));
                } else {
                    return ctx.thenPersist(new WalletEvent.CurrencyDeposited(entityId(), cmd.cryptocurrency, cmd.amount),
                            // Then once the event is successfully persisted, we respond with done.
                            evt -> ctx.reply(Done.getInstance()));
                }
            });

    b.setCommandHandler(WalletCommand.Send.class, (cmd, ctx) -> {
                WalletState walletState = state();
                String currency = cmd.cryptocurrency;

        BigDecimal previousAmount = walletState.balances.getOrDefault(currency, new BigDecimal(0.0));
        BigDecimal currentAmount = previousAmount.subtract(cmd.amount);

        if (currentAmount.compareTo(new BigDecimal(0.0)) >= 0){

                return ctx.thenPersist(new WalletEvent.CurrencySent(cmd.receivingWalletID, cmd.cryptocurrency, cmd.amount, entityId()),
                         evt -> ctx.reply(Done.getInstance()));
                } else {
                    ctx.invalidCommand("You have insufficient funds to make this transaction: " + cmd.toString());
                    return ctx.done();
                }
            });

      b.setEventHandler(WalletEvent.NewWalletCreated.class,
              // We simply update the current state to use the greeting message from
              // the event.

              evt -> {
                  WalletState walletState = state();
                  String currency = evt.currency;

                  BigDecimal previousAmount = walletState.balances.getOrDefault(currency, new BigDecimal(0.0));
                  BigDecimal currentAmount = previousAmount.add(evt.amount);

                  ImmutableMap.Builder<String, BigDecimal> parametersBuilder = ImmutableMap.builder();

                  parametersBuilder.put(currency, currentAmount);

                  walletState.balances.entrySet().stream().filter(k -> !k.getKey().equalsIgnoreCase(currency)).forEach(
                          entry -> parametersBuilder.put(entry)
                  );


                  return new WalletState(parametersBuilder.build(),  LocalDateTime.now().toString());
              });

    /*
     * Event handler for the GreetingMessageChanged event.
     */
    b.setEventHandler(WalletEvent.CurrencyDeposited.class,
        // We simply update the current state to use the greeting message from
        // the event.

        evt -> {
          WalletState walletState = state();
          String currency = evt.currency;

          BigDecimal previousAmount = walletState.balances.getOrDefault(currency, new BigDecimal(0.0));
            BigDecimal currentAmount = previousAmount.add(evt.amount);

          ImmutableMap.Builder<String, BigDecimal> parametersBuilder = ImmutableMap.builder();

          parametersBuilder.put(currency, currentAmount);

          walletState.balances.entrySet().stream().filter(k -> !k.getKey().equalsIgnoreCase(currency)).forEach(
                  entry -> parametersBuilder.put(entry)
          );


          return new WalletState(parametersBuilder.build(),  LocalDateTime.now().toString());
        });

    b.setEventHandler(WalletEvent.CurrencySent.class,
            evt -> {
                WalletState walletState = state();
                String currency = evt.currency;

                BigDecimal previousAmount = walletState.balances.getOrDefault(currency, new BigDecimal(0.0));
                BigDecimal currentAmount = previousAmount.subtract(evt.amount);

                ImmutableMap.Builder<String, BigDecimal> parametersBuilder = ImmutableMap.builder();

                parametersBuilder.put(currency, currentAmount);

                walletState.balances.entrySet().stream().filter(k -> !k.getKey().equalsIgnoreCase(currency)).forEach(
                        entry -> parametersBuilder.put(entry)
                );


                return new WalletState(parametersBuilder.build(),  LocalDateTime.now().toString());
            });


    /*
     * Command handler for the Hello command.
     */
    b.setReadOnlyCommandHandler(Hello.class,
        // Get the greeting from the current state, and prepend it to the name
        // that we're sending
        // a greeting to, and reply with that message.
        (cmd, ctx) -> ctx.reply("snapshot state is: "+ state().toString()));

    /*
     * We've defined all our behaviour, so build and return it.
     */
    return b.build();
  }


}

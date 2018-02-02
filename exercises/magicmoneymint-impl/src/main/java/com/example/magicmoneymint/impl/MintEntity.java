/*
 * 
 */
package com.example.magicmoneymint.impl;

import akka.Done;
import com.example.magicmoneymint.impl.MintCommand.UseGreetingMessage;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * This is an event sourced entity. It has a state, {@link MintState}, which
 * stores what the greeting should be (eg, "MintBalance").
 * <p>
 * Event sourced entities are interacted with by sending them commands. This
 * entity supports two commands, a {@link UseGreetingMessage} command, which is
 * used to change the greeting, and a {@link MintCommand.MintBalance} command, which is a read
 * only command which returns a greeting to the currency specified by the command.
 * <p>
 * Commands get translated to events, and it's the events that get persisted by
 * the entity. Each event will have an event handler registered for it, and an
 * event handler simply applies an event to the current state. This will be done
 * when the event is first created, and it will also be done when the entity is
 * loaded from the database - each event will be replayed to recreate the state
 * of the entity.
 * <p>
 */
public class MintEntity extends PersistentEntity<MintCommand, MintEvent, MagicMintState> {

  /**
   * An entity can define different behaviours for different states, but it will
   * always start with an initial behaviour. This entity only has one behaviour.
   */
  @Override
  public Behavior initialBehavior(Optional<MagicMintState> snapshotState) {

    /*
     * Behaviour is defined using a behaviour builder. The behaviour builder
     * starts with a state, if this entity supports snapshotting (an
     * optimisation that allows the state itself to be persisted to combine many
     * events into one), then the passed in snapshotState may have a value that
     * can be used.
     *
     * Otherwise, the default state is to use the MintBalance greeting.
     */
    BehaviorBuilder b = newBehaviorBuilder(
        snapshotState.orElse(new MagicMintState(new BigDecimal(0.0),entityId(), LocalDateTime.now().toString())));


    b.setCommandHandler(MintCommand.MakeMoney.class, (cmd, ctx) ->
            // In response to this command, we want to first persist it as a
            // MadeMoney event
            ctx.thenPersist(new MintEvent.MadeMoney(entityId(), cmd.amount),
                    // Then once the event is successfully persisted, we respond with done.
                    evt -> ctx.reply(Done.getInstance())));

    /*
     * Event handler for the MadeMoney event.
     */
    b.setEventHandler(MintEvent.MadeMoney.class,
        // We simply update the current state to use the greeting message from
        // the event.
        evt -> new MagicMintState(state().totalValue.add(evt.amount),evt.currency, LocalDateTime.now().toString()));

    /*
     * Command handler for the MintBalance command.
     */
    b.setReadOnlyCommandHandler(MintCommand.MintBalance.class,
        // Get the greeting from the current state, and prepend it to the currency
        // that we're sending
        // a greeting to, and reply with that message.
        (cmd, ctx) -> ctx.reply(state()+ "It's Magic!"));

    /*
     * We've defined all our behaviour, so build and return it.
     */
    return b.build();
  }

}

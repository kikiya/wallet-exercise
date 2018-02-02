package com.example.wallet.impl;

import akka.Done;
import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import com.example.wallet.impl.WalletCommand.CheckBalance;
import com.example.wallet.impl.WalletCommand.UseGreetingMessage;
import com.example.wallet.impl.WalletEvent.GreetingMessageChanged;
import com.lightbend.lagom.javadsl.testkit.PersistentEntityTestDriver;
import com.lightbend.lagom.javadsl.testkit.PersistentEntityTestDriver.Outcome;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class WalletEntityTest {

  static ActorSystem system;

  @BeforeClass
  public static void setup() {
    system = ActorSystem.create("WalletEntityTest");
  }

  @AfterClass
  public static void teardown() {
    JavaTestKit.shutdownActorSystem(system);
    system = null;
  }

  @Test
  public void testWalletEntity() {
    PersistentEntityTestDriver<WalletCommand, WalletEvent, WalletState> driver = new PersistentEntityTestDriver<>(system,
        new WalletEntity(), "world-1");

    Outcome<WalletEvent, WalletState> outcome1 = driver.run(new WalletCommand.CheckBalance("Alice"));
    assertEquals("CheckBalance, Alice!", outcome1.getReplies().get(0));
    assertEquals(Collections.emptyList(), outcome1.issues());

    Outcome<WalletEvent, WalletState> outcome2 = driver.run(new UseGreetingMessage("Hi"),
        new CheckBalance("Bob"));
    assertEquals(1, outcome2.events().size());
    assertEquals(new GreetingMessageChanged("world-1", "Hi"), outcome2.events().get(0));
    assertEquals("Hi", outcome2.state().message);
    assertEquals(Done.getInstance(), outcome2.getReplies().get(0));
    assertEquals("Hi, Bob!", outcome2.getReplies().get(1));
    assertEquals(2, outcome2.getReplies().size());
    assertEquals(Collections.emptyList(), outcome2.issues());
  }

}

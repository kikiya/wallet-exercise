package com.example.magicmoneymint.impl;

import akka.Done;
import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import com.example.magicmoneymint.impl.MintCommand.MintBalance;
import com.example.magicmoneymint.impl.MintCommand.UseGreetingMessage;
import com.lightbend.lagom.javadsl.testkit.PersistentEntityTestDriver;
import com.lightbend.lagom.javadsl.testkit.PersistentEntityTestDriver.Outcome;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class MintEntityTest {

  static ActorSystem system;

  @BeforeClass
  public static void setup() {
    system = ActorSystem.create("MintEntityTest");
  }

  @AfterClass
  public static void teardown() {
    JavaTestKit.shutdownActorSystem(system);
    system = null;
  }

  @Test
  public void testMagicmoneymintEntity() {
    PersistentEntityTestDriver<MintCommand, MintEvent, MintState> driver = new PersistentEntityTestDriver<>(system,
        new MintEntity(), "world-1");

    Outcome<MintEvent, MintState> outcome1 = driver.run(new MintCommand.MintBalance("Alice"));
    assertEquals("MintBalance, Alice!", outcome1.getReplies().get(0));
    assertEquals(Collections.emptyList(), outcome1.issues());

    Outcome<MintEvent, MintState> outcome2 = driver.run(new UseGreetingMessage("Hi"),
        new MintBalance("Bob"));
    assertEquals(1, outcome2.events().size());
    assertEquals(new GreetingMessageChanged("world-1", "Hi"), outcome2.events().get(0));
    assertEquals("Hi", outcome2.state().message);
    assertEquals(Done.getInstance(), outcome2.getReplies().get(0));
    assertEquals("Hi, Bob!", outcome2.getReplies().get(1));
    assertEquals(2, outcome2.getReplies().size());
    assertEquals(Collections.emptyList(), outcome2.issues());
  }

}

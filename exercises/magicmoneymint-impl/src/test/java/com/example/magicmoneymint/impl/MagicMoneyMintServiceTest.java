/*
 * 
 */
package com.example.magicmoneymint.impl;

import com.example.magicmoneymint.api.GreetingMessage;
import com.example.magicmoneymint.api.MagicMoneyMintService;
import org.junit.Test;

import static com.lightbend.lagom.javadsl.testkit.ServiceTest.defaultSetup;
import static com.lightbend.lagom.javadsl.testkit.ServiceTest.withServer;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

public class MagicMoneyMintServiceTest {

  @Test
  public void shouldStorePersonalizedGreeting() throws Exception {
    withServer(defaultSetup().withCassandra(), server -> {
      MagicMoneyMintService service = server.client(MagicMoneyMintService.class);

      String msg1 = service.checkBalance("Alice").invoke().toCompletableFuture().get(5, SECONDS);
      assertEquals("MintBalance, Alice!", msg1); // default greeting

      service.useGreeting("Alice").invoke(new GreetingMessage("Hi")).toCompletableFuture().get(5, SECONDS);
      String msg2 = service.checkBalance("Alice").invoke().toCompletableFuture().get(5, SECONDS);
      assertEquals("Hi, Alice!", msg2);

      String msg3 = service.checkBalance("Bob").invoke().toCompletableFuture().get(5, SECONDS);
      assertEquals("MintBalance, Bob!", msg3); // default greeting
    });
  }

}

/*
 * 
 */
package com.example.wallet.impl;

import com.example.wallet.api.GreetingMessage;
import com.example.wallet.api.WalletService;
import org.junit.Test;

import static com.lightbend.lagom.javadsl.testkit.ServiceTest.defaultSetup;
import static com.lightbend.lagom.javadsl.testkit.ServiceTest.withServer;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

public class WalletServiceTest {

  @Test
  public void shouldStorePersonalizedGreeting() throws Exception {
    withServer(defaultSetup().withCassandra(true), server -> {
      WalletService service = server.client(WalletService.class);

      String msg1 = service.balance("Alice").invoke().toCompletableFuture().get(5, SECONDS);
      assertEquals("CheckBalance, Alice!", msg1); // default greeting

      service.useGreeting("Alice").invoke(new GreetingMessage("Hi")).toCompletableFuture().get(5, SECONDS);
      String msg2 = service.balance("Alice").invoke().toCompletableFuture().get(5, SECONDS);
      assertEquals("Hi, Alice!", msg2);

      String msg3 = service.balance("Bob").invoke().toCompletableFuture().get(5, SECONDS);
      assertEquals("CheckBalance, Bob!", msg3); // default greeting
    });
  }

}

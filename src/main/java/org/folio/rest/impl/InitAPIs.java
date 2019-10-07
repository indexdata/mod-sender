package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import org.folio.rest.resource.interfaces.InitAPI;
import org.folio.sender.DeliveryVerticle;


public class InitAPIs implements InitAPI {

  public void init(Vertx vertx, Context context, Handler<AsyncResult<Boolean>> resultHandler) {
    context.put("httpClient", WebClient.create(vertx));
    Future<String> deployingFuture = Future.future();
    vertx.deployVerticle(new DeliveryVerticle(), deployingFuture.completer());
    deployingFuture.map(true).setHandler(resultHandler);
  }
}

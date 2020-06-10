package org.folio.rest.impl;

import org.folio.rest.resource.interfaces.InitAPI;
import org.folio.sender.DeliveryVerticle;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;


public class InitAPIs implements InitAPI {

  public void init(Vertx vertx, Context context, Handler<AsyncResult<Boolean>> resultHandler) {
    context.put("webClient", WebClient.create(vertx));
    Promise<String> promise = Promise.promise();
    vertx.deployVerticle(new DeliveryVerticle(), promise);
    promise.future().map(true).onComplete(resultHandler);
  }
}

package org.folio.rest.impl;

import io.vertx.core.*;
import io.vertx.ext.web.client.WebClient;
import org.folio.rest.resource.interfaces.InitAPI;
import org.folio.sender.DeliveryVerticle;


public class InitAPIs implements InitAPI {

  public void init(Vertx vertx, Context context, Handler<AsyncResult<Boolean>> resultHandler) {
    context.put("webClient", WebClient.create(vertx));
    Promise<String> promise = Promise.promise();
    vertx.deployVerticle(new DeliveryVerticle(), promise);
    promise.future().map(true).setHandler(resultHandler);
  }
}

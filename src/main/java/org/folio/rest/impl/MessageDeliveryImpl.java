package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import org.folio.rest.jaxrs.model.Message;
import org.folio.rest.jaxrs.resource.MessageDelivery;

import javax.ws.rs.core.Response;
import java.util.Map;

public class MessageDeliveryImpl implements MessageDelivery {

  @Override
  public void postMessageDelivery(Message entity,
                                  Map<String, String> okapiHeaders,
                                  Handler<AsyncResult<Response>> asyncResultHandler,
                                  Context vertxContext) {
    //TODO replace stub response
    asyncResultHandler.handle(Future.succeededFuture(PostMessageDeliveryResponse.respond204WithTextPlain("")));
  }
}

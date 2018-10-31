package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import org.folio.rest.jaxrs.model.Notification;
import org.folio.rest.jaxrs.resource.MessageDelivery;
import org.folio.rest.model.OkapiHeaders;
import org.folio.sender.SenderService;
import org.folio.sender.SenderServiceImpl;
import org.folio.sender.util.SenderHelper;

import javax.ws.rs.core.Response;
import java.util.Map;

public class MessageDeliveryImpl implements MessageDelivery {

  private SenderService senderService;

  public MessageDeliveryImpl(Vertx vertx, String tenantId) {
    senderService = new SenderServiceImpl(vertx);
  }

  @Override
  public void postMessageDelivery(Notification entity,
                                  Map<String, String> okapiHeaders,
                                  Handler<AsyncResult<Response>> asyncResultHandler,
                                  Context vertxContext) {
    try {
      senderService.sendNotification(entity, new OkapiHeaders(okapiHeaders));
      asyncResultHandler.handle(Future.succeededFuture(PostMessageDeliveryResponse.respond204WithTextPlain("")));
    } catch (Exception e) {
      asyncResultHandler.handle(Future.succeededFuture(
        SenderHelper.mapExceptionToResponse(e)));
    }
  }
}

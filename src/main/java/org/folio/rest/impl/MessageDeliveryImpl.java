package org.folio.rest.impl;

import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.folio.rest.jaxrs.model.Notification;
import org.folio.rest.jaxrs.resource.MessageDelivery;
import org.folio.rest.model.OkapiHeaders;
import org.folio.rest.tools.utils.TenantTool;
import org.folio.sender.SenderService;
import org.folio.sender.SenderServiceImpl;
import org.folio.sender.util.ExceptionHelper;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

public class MessageDeliveryImpl implements MessageDelivery {

  private SenderService senderService;
  private String tenantId;

  public MessageDeliveryImpl(Vertx vertx, String tenantId) {
    senderService = new SenderServiceImpl(vertx);
    this.tenantId = TenantTool.calculateTenantId(tenantId);
  }

  @Override
  public void postMessageDelivery(Notification entity,
                                  Map<String, String> okapiHeaders,
                                  Handler<AsyncResult<Response>> asyncResultHandler,
                                  Context vertxContext) {
    try {
      OkapiHeaders headers = new OkapiHeaders(okapiHeaders);
      headers.setTenant(tenantId);
      senderService.sendNotification(entity, headers)
        .map(PostMessageDeliveryResponse.respond204WithTextPlain(StringUtils.EMPTY))
        .map(Response.class::cast)
        .otherwise(ExceptionHelper::handleException)
        .onComplete(asyncResultHandler);
    } catch (Exception e) {
      asyncResultHandler.handle(Future.succeededFuture(
        ExceptionHelper.handleException(e)));
    }
  }
}

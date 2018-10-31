package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.serviceproxy.ServiceBinder;
import org.folio.rest.resource.interfaces.InitAPI;
import org.folio.sender.delivery.DeliveryChannel;
import org.folio.sender.delivery.EmailDeliveryChannel;
import org.folio.sender.util.SenderHelper;


public class InitAPIs implements InitAPI {

  public void init(Vertx vertx, Context context, Handler<AsyncResult<Boolean>> resultHandler) {
    registerDeliveryChannel("email", "delivery-channel.email.queue",
      new EmailDeliveryChannel(vertx), vertx);
    resultHandler.handle(Future.succeededFuture(true));
  }

  private <T extends DeliveryChannel> void registerDeliveryChannel(String name, String address,
                                                                   T deliveryChannelInstance, Vertx vertx) {
    LocalMap<String, String> deliveryCahnnelAddressesMap = vertx.sharedData()
      .getLocalMap(SenderHelper.DELIVERY_CHANNELS_LOCAL_MAP);

    new ServiceBinder(vertx).setAddress(address).register(DeliveryChannel.class, deliveryChannelInstance);
    deliveryCahnnelAddressesMap.put(name, address);
  }
}

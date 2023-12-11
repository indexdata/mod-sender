package org.folio.sender;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.serviceproxy.ServiceBinder;
import org.folio.sender.delivery.DeliveryChannel;
import org.folio.sender.delivery.EmailDeliveryChannel;
import org.folio.sender.delivery.MailDeliveryChannel;

public class DeliveryVerticle extends AbstractVerticle {

  public static final String DELIVERY_CHANNELS_LOCAL_MAP = "delivery-channel.map";

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    registerDeliveryChannel("email", "delivery-channel.email.queue", vertx,
      new EmailDeliveryChannel(vertx, "/email"));
    registerDeliveryChannel("mail", "delivery-channel.mail.queue", vertx,
      new MailDeliveryChannel(vertx, "/mail"));

    startPromise.handle(Future.succeededFuture());
  }

  private <T extends DeliveryChannel> void registerDeliveryChannel(String name, String address, Vertx vertx,
                                                                   T deliveryChannelInstance) {
    LocalMap<String, String> deliveryCahnnelAddressesMap = vertx.sharedData()
      .getLocalMap(DELIVERY_CHANNELS_LOCAL_MAP);

    new ServiceBinder(vertx).setAddress(address).register(DeliveryChannel.class, deliveryChannelInstance);
    deliveryCahnnelAddressesMap.put(name, address);
  }
}

package org.folio.sender;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.folio.rest.jaxrs.model.Channel;
import org.folio.rest.jaxrs.model.Notification;
import org.folio.rest.model.OkapiHeaders;
import org.folio.sender.delivery.DeliveryChannel;
import org.folio.sender.util.SenderHelper;

import java.util.List;
import java.util.Map;

public class SenderServiceImpl implements SenderService {

  private Vertx vertx;
  private Map<String, String> deliveryChannelAddressesMap;

  public SenderServiceImpl(Vertx vertx) {
    this.vertx = vertx;
    deliveryChannelAddressesMap = vertx.sharedData().getLocalMap(SenderHelper.DELIVERY_CHANNELS_LOCAL_MAP);
  }

  public void sendNotification(Notification notification, OkapiHeaders okapiHeaders) {
    validateDeliveryChannels(notification.getChannels());
    for (Channel channel : notification.getChannels()) {
      String deliveryChannelAddress = deliveryChannelAddressesMap.get(channel.getType());
      DeliveryChannel deliveryChannelProxy = DeliveryChannel.createProxy(vertx, deliveryChannelAddress);
      deliveryChannelProxy.deliverMessage(notification.getNotificationId(),
        JsonObject.mapFrom(channel), JsonObject.mapFrom(okapiHeaders));
    }
  }

  private void validateDeliveryChannels(List<Channel> deliveryChannels) {
    for (Channel channel : deliveryChannels) {
      boolean deliveryChannelIsSupported = deliveryChannelAddressesMap.containsKey(channel.getType());
      if (!deliveryChannelIsSupported) {
        String errorMessage = String.format("Delivery channel '%s' is not supported", channel.getType());
        throw new IllegalStateException(errorMessage);
      }
    }
  }
}


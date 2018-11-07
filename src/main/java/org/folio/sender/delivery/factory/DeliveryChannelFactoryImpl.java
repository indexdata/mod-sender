package org.folio.sender.delivery.factory;

import io.vertx.core.Vertx;
import org.folio.sender.DeliveryVerticle;
import org.folio.sender.delivery.DeliveryChannel;
import org.folio.sender.delivery.DeliveryChannelVertxEBProxy;

import java.util.Map;

public class DeliveryChannelFactoryImpl implements DeliveryChannelFactory {

  private Vertx vertx;
  private Map<String, String> deliveryChannelAddressesMap;

  public DeliveryChannelFactoryImpl(Vertx vertx) {
    this.vertx = vertx;
    deliveryChannelAddressesMap = vertx.sharedData().getLocalMap(DeliveryVerticle.DELIVERY_CHANNELS_LOCAL_MAP);
  }

  @Override
  public DeliveryChannel createProxy(String deliveryChannelType) {
    String address = deliveryChannelAddressesMap.get(deliveryChannelType);
    if (address == null) {
      String exceptionMessage =
        String.format("Delivery channel factory does not support '%s' delivery channel", deliveryChannelType);
      throw new IllegalArgumentException(exceptionMessage);
    }
    return new DeliveryChannelVertxEBProxy(vertx, address);
  }

  @Override
  public boolean supportsDeliveryChannel(String deliveryChannelType) {
    return deliveryChannelAddressesMap.containsKey(deliveryChannelType);
  }
}

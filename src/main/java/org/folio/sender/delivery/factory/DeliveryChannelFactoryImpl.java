package org.folio.sender.delivery.factory;

import io.vertx.core.Vertx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.folio.sender.DeliveryVerticle;
import org.folio.sender.delivery.DeliveryChannel;
import org.folio.sender.delivery.DeliveryChannelVertxEBProxy;

import java.util.Map;

public class DeliveryChannelFactoryImpl implements DeliveryChannelFactory {

  private static final Logger log = LogManager.getLogger(DeliveryChannelFactoryImpl.class);
  private Vertx vertx;
  private Map<String, String> deliveryChannelAddressesMap;

  public DeliveryChannelFactoryImpl(Vertx vertx) {
    this.vertx = vertx;
    deliveryChannelAddressesMap = vertx.sharedData().getLocalMap(DeliveryVerticle.DELIVERY_CHANNELS_LOCAL_MAP);
  }

  @Override
  public DeliveryChannel createProxy(String deliveryChannelType) {
    log.debug("createProxy:: creating DeliveryChannel for deliveryChannelType {}", deliveryChannelType);
    String address = deliveryChannelAddressesMap.get(deliveryChannelType);
    if (address == null) {
      String exceptionMessage =
        String.format("createProxy:: Delivery channel factory does not support '%s' delivery channel", deliveryChannelType);
      log.warn(exceptionMessage);
      throw new IllegalArgumentException(exceptionMessage);
    }
    return new DeliveryChannelVertxEBProxy(vertx, address);
  }

  @Override
  public boolean supportsDeliveryChannel(String deliveryChannelType) {
    log.debug("supportsDeliveryChannel:: checking deliveryChannelAddressesMap contains deliveryChannelType {}", deliveryChannelType);
    return deliveryChannelAddressesMap.containsKey(deliveryChannelType);
  }
}

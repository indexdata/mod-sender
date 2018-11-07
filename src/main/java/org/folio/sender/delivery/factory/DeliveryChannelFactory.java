package org.folio.sender.delivery.factory;

import org.folio.sender.delivery.DeliveryChannel;

public interface DeliveryChannelFactory {

  DeliveryChannel createProxy(String deliveryChannelType);

  boolean supportsDeliveryChannel(String deliveryChannelType);
}

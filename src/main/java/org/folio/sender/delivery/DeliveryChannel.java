package org.folio.sender.delivery;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

@ProxyGen
public interface DeliveryChannel {

  static DeliveryChannel createProxy(Vertx vertx, String address) {
    return new DeliveryChannelVertxEBProxy(vertx, address);
  }

  void deliverMessage(String notificationId, JsonObject message, JsonObject okapiHeadersJson);
}

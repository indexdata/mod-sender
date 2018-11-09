package org.folio.sender.delivery;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.json.JsonObject;

@ProxyGen
public interface DeliveryChannel {

  void deliverMessage(String notificationId, JsonObject recipientJson, JsonObject message, JsonObject okapiHeadersJson);
}

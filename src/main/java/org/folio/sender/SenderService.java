package org.folio.sender;

import io.vertx.core.Future;
import org.folio.rest.jaxrs.model.Notification;
import org.folio.rest.jaxrs.model.User;
import org.folio.rest.model.OkapiHeaders;

/**
 * Service for notifications distribution to {@link org.folio.sender.delivery.DeliveryChannel}
 */
public interface SenderService {

  /**
   * Sends messages to registered delivery channels
   *
   * @param notification notification
   * @param okapiHeaders okapiHeaders
   */
  Future<User> sendNotification(Notification notification, OkapiHeaders okapiHeaders);

}

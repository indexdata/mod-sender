package org.folio.sender;

import org.folio.rest.jaxrs.model.Notification;
import org.folio.rest.model.OkapiHeaders;

public interface SenderService {

  void sendNotification(Notification notification, OkapiHeaders okapiHeaders);

}

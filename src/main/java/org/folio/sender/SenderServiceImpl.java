package org.folio.sender;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.folio.rest.jaxrs.model.Message;
import org.folio.rest.jaxrs.model.Notification;
import org.folio.rest.jaxrs.model.User;
import org.folio.rest.model.OkapiHeaders;
import org.folio.sender.delivery.DeliveryChannel;
import org.folio.sender.delivery.factory.DeliveryChannelFactory;
import org.folio.sender.delivery.factory.DeliveryChannelFactoryImpl;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.List;

public class SenderServiceImpl implements SenderService {

  private static final Logger log = LogManager.getLogger(SenderServiceImpl.class);
  private DeliveryChannelFactory deliveryChannelFactory;
  private WebClient webClient;

  public SenderServiceImpl(Vertx vertx) {
    this.deliveryChannelFactory = new DeliveryChannelFactoryImpl(vertx);
    this.webClient = vertx.getOrCreateContext().get("webClient");
  }

  public Future<User> sendNotification(Notification notification, OkapiHeaders okapiHeaders) {
    log.debug("sendNotification:: notificationId {}, recipientUserId {}", notification.getNotificationId(), notification.getRecipientUserId());
    validateDeliveryChannels(notification.getMessages());
    return lookupUser(notification.getRecipientUserId(), okapiHeaders)
      .compose(user -> {
        for (Message message : notification.getMessages()) {
          DeliveryChannel deliveryChannelProxy = deliveryChannelFactory.createProxy(message.getDeliveryChannel());
          deliveryChannelProxy.deliverMessage(notification.getNotificationId(), JsonObject.mapFrom(user),
            JsonObject.mapFrom(message), JsonObject.mapFrom(okapiHeaders));
        }
        return Future.succeededFuture(user);
      });
  }

  private void validateDeliveryChannels(List<Message> messages) {
    log.debug("validateDeliveryChannels:: validating messages {} ", messages);
    for (Message message : messages) {
      if (!deliveryChannelFactory.supportsDeliveryChannel(message.getDeliveryChannel())) {
        String errorMessage = String.format("validateDeliveryChannels:: Delivery channel '%s' is not supported", message.getDeliveryChannel());
        log.warn(errorMessage);
        throw new BadRequestException(errorMessage);
      }
    }
  }

  private Future<User> lookupUser(String userId, OkapiHeaders okapiHeaders) {
    log.debug("lookupUser:: Fetching user details for userId {}", userId);
    String url = okapiHeaders.getOkapiUrl() + "/users/" + userId;
    HttpRequest<Buffer> request = webClient.getAbs(url);
    okapiHeaders.fillRequestHeaders(request.headers());
    request.putHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);

    Promise<HttpResponse<Buffer>> promise = Promise.promise();
    request.send(promise);
    return promise.future().map(response -> {
      switch (response.statusCode()) {
        case HttpStatus.SC_OK:
          return response.bodyAsJson(User.class);
        case HttpStatus.SC_NOT_FOUND:
          throw new BadRequestException(response.bodyAsString());
        default:
          throw new InternalServerErrorException();
      }
    });
  }
}


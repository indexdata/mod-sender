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

  private DeliveryChannelFactory deliveryChannelFactory;
  private WebClient webClient;

  public SenderServiceImpl(Vertx vertx) {
    this.deliveryChannelFactory = new DeliveryChannelFactoryImpl(vertx);
    this.webClient = vertx.getOrCreateContext().get("webClient");
  }

  public Future<Void> sendNotification(Notification notification, OkapiHeaders okapiHeaders) {
    validateDeliveryChannels(notification.getMessages());
    return lookupUser(notification.getRecipientUserId(), okapiHeaders)
      .compose(user -> {
        for (Message message : notification.getMessages()) {
          DeliveryChannel deliveryChannelProxy = deliveryChannelFactory.createProxy(message.getDeliveryChannel());
          deliveryChannelProxy.deliverMessage(notification.getNotificationId(), JsonObject.mapFrom(user),
            JsonObject.mapFrom(message), JsonObject.mapFrom(okapiHeaders));
        }
        return Future.succeededFuture();
      });
  }

  private void validateDeliveryChannels(List<Message> messages) {
    for (Message message : messages) {
      if (!deliveryChannelFactory.supportsDeliveryChannel(message.getDeliveryChannel())) {
        String errorMessage = String.format("Delivery channel '%s' is not supported", message.getDeliveryChannel());
        throw new BadRequestException(errorMessage);
      }
    }
  }

  private Future<User> lookupUser(String userId, OkapiHeaders okapiHeaders) {
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


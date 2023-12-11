package org.folio.sender.delivery;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.folio.rest.jaxrs.model.EmailEntity;
import org.folio.rest.jaxrs.model.User;

public class MailDeliveryChannel implements DeliveryChannel {

  private static final Logger LOG = LogManager.getLogger(MailDeliveryChannel.class);

  private final WebClient webClient;
  private final String mailUrlPath;

  public MailDeliveryChannel(Vertx vertx, String mailUrlPath) {
    this.webClient = WebClient.create(vertx);
    this.mailUrlPath = mailUrlPath;
  }

  @Override
  public void deliverMessage(String notificationId, JsonObject recipientJson,
                             JsonObject message, JsonObject okapiHeadersJson) {
    LOG.debug("deliverMessage:: Sending message to recipient {} with message {}",
        recipientJson, message);
    try {
      User recipient = recipientJson.mapTo(User.class);
      EmailEntity emailEntity = message.mapTo(EmailEntity.class);
      emailEntity.setNotificationId(notificationId);
      emailEntity.setTo(getTo(recipient));

      HttpRequest<Buffer> request = EmailDeliveryChannel.createRequestAndPrepareHeaders(
          okapiHeadersJson, mailUrlPath, webClient);

      request.sendJson(emailEntity, response -> {
        if (response.failed()) {
          LOG.error("deliverMessage:: Error from Mail module {} ", response.cause().getMessage());
        } else if (response.result().statusCode() != HttpStatus.SC_OK) {
          String errorMessage = String.format("deliverMessage:: Mail module responded with "
              + "status '%s' and body '%s'",
              response.result().statusCode(), response.result().bodyAsString());
          LOG.error(errorMessage);
        }
      });
    } catch (Exception e) {
      LOG.error("deliverMessage:: Error while attempting to "
          + "deliver message to recipient {} ", recipientJson, e);
    }
  }

  private String getTo(User recipient) {
    if (recipient.getPersonal() != null) {
      return recipient.getPersonal().getLastName() + ","
        + recipient.getPersonal().getFirstName() + "," + recipient.getPersonal().getEmail();
    } else {
      return recipient.getId();
    }
  }
}

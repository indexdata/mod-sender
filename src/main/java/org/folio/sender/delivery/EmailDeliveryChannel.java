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
import org.folio.rest.model.OkapiHeaders;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

public class EmailDeliveryChannel implements DeliveryChannel {

  private static final Logger LOG = LogManager.getLogger(EmailDeliveryChannel.class);

  private WebClient webClient;
  private String emailUrlPath;

  public EmailDeliveryChannel(Vertx vertx, String emailUrlPath) {
    this.webClient = WebClient.create(vertx);
    this.emailUrlPath = emailUrlPath;
  }

  @Override
  public void deliverMessage(String notificationId, JsonObject recipientJson,
                             JsonObject message, JsonObject okapiHeadersJson) {
    LOG.debug("deliverMessage:: Sending message to recipient {} with message {}", recipientJson, message);
    try {
      User recipient = recipientJson.mapTo(User.class);
      EmailEntity emailEntity = message.mapTo(EmailEntity.class);
      emailEntity.setNotificationId(notificationId);
      emailEntity.setTo(recipient.getPersonal().getEmail());

      OkapiHeaders okapiHeaders = okapiHeadersJson.mapTo(OkapiHeaders.class);
      String url = okapiHeaders.getOkapiUrl() + emailUrlPath;
      HttpRequest<Buffer> request = webClient.postAbs(url);
      okapiHeaders.fillRequestHeaders(request.headers());
      request.putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
      request.putHeader(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN);

      request.sendJson(emailEntity, response -> {
        if (response.failed()) {
          LOG.error("deliverMessage:: Error from Email module {} ", response.cause().getMessage());
        } else if (response.result().statusCode() != HttpStatus.SC_OK) {
          String errorMessage = String.format("deliverMessage:: Email module responded with status '%s' and body '%s'",
            response.result().statusCode(), response.result().bodyAsString());
          LOG.error(errorMessage);
        }
      });
    } catch (Exception e) {
      LOG.error("deliverMessage:: Error while attempting to deliver message to recipient {} ", recipientJson, e);
    }
  }
}

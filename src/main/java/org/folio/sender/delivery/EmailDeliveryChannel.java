package org.folio.sender.delivery;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import org.apache.http.HttpStatus;
import org.folio.rest.model.OkapiHeaders;
import org.folio.sender.delivery.model.EmailEntity;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

public class EmailDeliveryChannel implements DeliveryChannel {

  public static final Logger LOG = LoggerFactory.getLogger(EmailDeliveryChannel.class);
  private static final String EMAIL_PATH = "/email";

  private WebClient webClient;

  public EmailDeliveryChannel(Vertx vertx) {
    webClient = WebClient.create(vertx);
  }

  @Override
  public void deliverMessage(String notificationId, JsonObject message, JsonObject okapiHeadersJson) {
    EmailEntity emailEntity = message.mapTo(EmailEntity.class);
    emailEntity.setNotificationId(notificationId);

    OkapiHeaders okapiHeaders = okapiHeadersJson.mapTo(OkapiHeaders.class);
    String url = okapiHeaders.getOkapiUrl() + EMAIL_PATH;
    HttpRequest<Buffer> request = webClient.postAbs(url);
    okapiHeaders.fillRequestHeaders(request.headers());
    request.putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
    request.putHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);

    request.send(response -> {
      if (response.failed()) {
        LOG.error(response.cause().getMessage(), response.cause());
      } else if (response.result().statusCode() != HttpStatus.SC_OK) {
        String errorMessage = String.format("Email module responded with status '%s' and body '%s'",
          response.result().statusCode(), response.result().bodyAsString());
        LOG.error(errorMessage);
      }
    });
  }
}

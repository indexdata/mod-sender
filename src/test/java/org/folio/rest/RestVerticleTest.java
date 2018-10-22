package org.folio.rest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.http.HttpStatus;
import org.folio.rest.jaxrs.model.Message;
import org.folio.rest.tools.utils.NetworkUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.UUID;

@RunWith(VertxUnitRunner.class)
public class RestVerticleTest {

  private static final String TENANT = "diku";
  private static final String MESSAGE_DELIVERY_PATH = "/message-delivery";

  private static Vertx vertx;
  private static int port;
  private static RequestSpecification spec;

  @BeforeClass
  public static void setUp(TestContext context) {
    vertx = Vertx.vertx();
    port = NetworkUtils.nextFreePort();
    DeploymentOptions options = new DeploymentOptions()
      .setConfig(new JsonObject().put("http.port", port));
    vertx.deployVerticle(RestVerticle.class.getName(), options, context.asyncAssertSuccess());

    spec = new RequestSpecBuilder()
      .setContentType(ContentType.JSON)
      .setBaseUri("http://localhost:" + port)
      .addHeader(RestVerticle.OKAPI_HEADER_TENANT, TENANT)
      .build();
  }

  @AfterClass
  public static void tearDown(TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }

  @Test
  public void testGetTemplateStub(TestContext context) {
    //TODO Replace testing stub
    Message message = new Message()
      .withNotificationId(UUID.randomUUID().toString())
      .withChannels(Collections.emptyList());
    RestAssured.given()
      .spec(spec)
      .body(toJson(message))
      .when()
      .post(MESSAGE_DELIVERY_PATH)
      .then()
      .statusCode(HttpStatus.SC_NO_CONTENT);
  }

  private String toJson(Object object) {
    return JsonObject.mapFrom(object).toString();
  }
}

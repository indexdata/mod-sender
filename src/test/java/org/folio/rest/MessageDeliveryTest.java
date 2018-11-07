package org.folio.rest;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.http.HttpStatus;
import org.folio.rest.jaxrs.model.Message;
import org.folio.rest.jaxrs.model.Notification;
import org.folio.rest.jaxrs.model.Personal;
import org.folio.rest.jaxrs.model.User;
import org.folio.rest.model.OkapiHeaders;
import org.folio.rest.tools.utils.NetworkUtils;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.UUID;

@RunWith(VertxUnitRunner.class)
public class MessageDeliveryTest {

  private static final String TENANT = "diku";
  private static final String MESSAGE_DELIVERY_PATH = "/message-delivery";

  private static Vertx vertx;
  private static int port;
  private static RequestSpecification spec;

  @org.junit.Rule
  public WireMockRule mockServer = new WireMockRule(
    WireMockConfiguration.wireMockConfig()
      .dynamicPort()
      .notifier(new ConsoleNotifier(true)));

  private Header mockUrlHeader;

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

  @Before
  public void setUp() throws Exception {
    mockUrlHeader = new Header(OkapiHeaders.OKAPI_URL, "http://localhost:" + mockServer.port());
  }

  @AfterClass
  public static void tearDown(TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }


  @Test
  public void shouldReturn422WhenInvalidBody() {
    JsonObject emptyBody = new JsonObject();
    RestAssured.given()
      .spec(spec)
      .body(emptyBody.toString())
      .when()
      .post(MESSAGE_DELIVERY_PATH)
      .then()
      .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }

  @Test
  public void shouldReturnBadRequestWhenDeliveryChannelIsNotSupported() {
    mockEmailModule();

    Message notExistingChannel = new Message()
      .withDeliveryChannel("not_existing_channel").withFrom("from");
    Message emailChannel = new Message()
      .withDeliveryChannel("email").withFrom("from");

    Notification notification = new Notification()
      .withNotificationId(UUID.randomUUID().toString())
      .withRecipientUserId("userId")
      .withMessages(Arrays.asList(notExistingChannel, emailChannel));

    String textBody = RestAssured.given()
      .spec(spec)
      .body(toJson(notification))
      .when()
      .post(MESSAGE_DELIVERY_PATH)
      .then()
      .statusCode(HttpStatus.SC_BAD_REQUEST)
      .extract().asString();
    Assert.assertThat(textBody, Matchers.containsString("not_existing_channel"));
  }

  @Test
  public void shouldReturnNoContentAndSendEmailWhenRequestIsValid() {
    User mockRecipient = new User()
      .withId(UUID.randomUUID().toString())
      .withPersonal(new Personal().withEmail("test@test.com"));
    mockUserModule(mockRecipient.getId(), mockRecipient);
    mockEmailModule();

    Message emailChannel1 = new Message()
      .withDeliveryChannel("email")
      .withHeader("header1")
      .withBody("body1")
      .withOutputFormat(MediaType.TEXT_PLAIN);
    Message emailChannel2 = new Message()
      .withDeliveryChannel("email")
      .withHeader("header2")
      .withBody("body2")
      .withOutputFormat(MediaType.TEXT_HTML);

    Notification notification = new Notification()
      .withNotificationId(UUID.randomUUID().toString())
      .withRecipientUserId(mockRecipient.getId())
      .withMessages(Arrays.asList(emailChannel1, emailChannel2));

    RestAssured.given()
      .spec(spec)
      .header(mockUrlHeader)
      .body(toJson(notification))
      .when()
      .post(MESSAGE_DELIVERY_PATH)
      .then()
      .statusCode(HttpStatus.SC_NO_CONTENT);

    WireMock.verify(1, WireMock.getRequestedFor(
      WireMock.urlMatching("/users/" + mockRecipient.getId())));
  }

  private String toJson(Object object) {
    return JsonObject.mapFrom(object).toString();
  }

  private void mockEmailModule() {
    WireMock.stubFor(WireMock.post("/email")
      .willReturn(WireMock.ok()));
  }

  private void mockUserModule(String userId, User response) {
    WireMock.stubFor(WireMock.get("/users/" + userId)
      .willReturn(WireMock.okJson(JsonObject.mapFrom(response).toString())));
  }
}

package org.folio.sender.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.folio.rest.jaxrs.model.Attachment;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
  "notificationId",
  "from",
  "to",
  "header",
  "body",
  "attachments"
})
public class EmailEntity {

  /**
   * notification identifier
   * (Required)
   */
  @JsonProperty("notificationId")
  @JsonPropertyDescription("notification identifier")
  @NotNull
  private String notificationId;
  /**
   * sender's address
   * (Required)
   */
  @JsonProperty("from")
  @JsonPropertyDescription("sender's address")
  @NotNull
  private String from;
  /**
   * address of the recipient
   * (Required)
   */
  @JsonProperty("to")
  @JsonPropertyDescription("address of the recipient")
  @NotNull
  private String to;
  /**
   * subject of email
   * (Required)
   */
  @JsonProperty("header")
  @JsonPropertyDescription("subject of email")
  @NotNull
  private String header;
  /**
   * text of email
   * (Required)
   */
  @JsonProperty("body")
  @JsonPropertyDescription("text of email")
  @NotNull
  private String body;
  /**
   * attachment list
   */
  @JsonProperty("attachments")
  @JsonPropertyDescription("attachment list")
  @Valid
  private List<Attachment> attachments = new ArrayList<Attachment>();

  /**
   * notification identifier
   * (Required)
   */
  @JsonProperty("notificationId")
  public String getNotificationId() {
    return notificationId;
  }

  /**
   * notification identifier
   * (Required)
   */
  @JsonProperty("notificationId")
  public void setNotificationId(String notificationId) {
    this.notificationId = notificationId;
  }

  public EmailEntity withNotificationId(String notificationId) {
    this.notificationId = notificationId;
    return this;
  }

  /**
   * sender's address
   * (Required)
   */
  @JsonProperty("from")
  public String getFrom() {
    return from;
  }

  /**
   * sender's address
   * (Required)
   */
  @JsonProperty("from")
  public void setFrom(String from) {
    this.from = from;
  }

  public EmailEntity withFrom(String from) {
    this.from = from;
    return this;
  }

  /**
   * address of the recipient
   * (Required)
   */
  @JsonProperty("to")
  public String getTo() {
    return to;
  }

  /**
   * address of the recipient
   * (Required)
   */
  @JsonProperty("to")
  public void setTo(String to) {
    this.to = to;
  }

  public EmailEntity withTo(String to) {
    this.to = to;
    return this;
  }

  /**
   * subject of email
   * (Required)
   */
  @JsonProperty("header")
  public String getHeader() {
    return header;
  }

  /**
   * subject of email
   * (Required)
   */
  @JsonProperty("header")
  public void setHeader(String header) {
    this.header = header;
  }

  public EmailEntity withHeader(String header) {
    this.header = header;
    return this;
  }

  /**
   * text of email
   * (Required)
   */
  @JsonProperty("body")
  public String getBody() {
    return body;
  }

  /**
   * text of email
   * (Required)
   */
  @JsonProperty("body")
  public void setBody(String body) {
    this.body = body;
  }

  public EmailEntity withBody(String body) {
    this.body = body;
    return this;
  }

  /**
   * attachment list
   */
  @JsonProperty("attachments")
  public List<Attachment> getAttachments() {
    return attachments;
  }

  /**
   * attachment list
   */
  @JsonProperty("attachments")
  public void setAttachments(List<Attachment> attachments) {
    this.attachments = attachments;
  }

  public EmailEntity withAttachments(List<Attachment> attachments) {
    this.attachments = attachments;
    return this;
  }
}

package org.folio.rest.model;

import io.vertx.core.MultiMap;
import org.folio.rest.RestVerticle;
import org.folio.rest.tools.utils.TenantTool;

import java.util.Map;

/**
 * Okapi headers
 */
public class OkapiHeaders {

  public static final String OKAPI_URL = "x-okapi-url";

  private String token;
  private String tenant;
  private String okapiUrl;

  public OkapiHeaders() {
  }

  public OkapiHeaders(Map<String, String> headers) {
    token = headers.get(RestVerticle.OKAPI_HEADER_TOKEN);
    tenant = TenantTool.calculateTenantId(headers.get(RestVerticle.OKAPI_HEADER_TENANT));
    okapiUrl = headers.get(OKAPI_URL);
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getTenant() {
    return tenant;
  }

  public void setTenant(String tenant) {
    this.tenant = tenant;
  }

  public String getOkapiUrl() {
    return okapiUrl;
  }

  public void setOkapiUrl(String okapiUrl) {
    this.okapiUrl = okapiUrl;
  }

  public void fillRequestHeaders(MultiMap headers) {
    if (token != null) {
      headers.add(RestVerticle.OKAPI_HEADER_TOKEN, token);
    }
    if (tenant != null) {
      headers.add(RestVerticle.OKAPI_HEADER_TENANT, tenant);
    }
    if (okapiUrl != null) {
      headers.add(OKAPI_URL, okapiUrl);
    }
  }
}

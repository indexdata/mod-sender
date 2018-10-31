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

  public OkapiHeaders(Map<String, String> headers) {
    token = TenantTool.calculateTenantId(headers.get(RestVerticle.OKAPI_HEADER_TOKEN));
    tenant = headers.get(RestVerticle.OKAPI_HEADER_TENANT);
    okapiUrl = headers.get(OKAPI_URL);
  }

  public String getToken() {
    return token;
  }

  public String getTenant() {
    return tenant;
  }

  public String getOkapiUrl() {
    return okapiUrl;
  }

  public void fillRequestHeaders(MultiMap headers) {
    headers.add(RestVerticle.OKAPI_HEADER_TOKEN, token);
    headers.add(RestVerticle.OKAPI_HEADER_TENANT, tenant);
    headers.add(OKAPI_URL, okapiUrl);
  }
}

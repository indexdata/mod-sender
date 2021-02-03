package org.folio.sender.util;

import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public final class ExceptionHelper {

  private static final Logger LOG = LogManager.getLogger("mod-sender");

  private ExceptionHelper() {
  }

  public static Response handleException(Throwable throwable) {
    if (throwable.getClass() == BadRequestException.class) {
      return Response.status(HttpStatus.SC_BAD_REQUEST)
        .type(MediaType.TEXT_PLAIN)
        .entity(throwable.getMessage())
        .build();
    }
    LOG.error(throwable.getMessage(), throwable);
    return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
      .type(MediaType.TEXT_PLAIN)
      .entity(Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase())
      .build();
  }
}

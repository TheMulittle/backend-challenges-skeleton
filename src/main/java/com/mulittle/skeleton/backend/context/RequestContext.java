package com.mulittle.skeleton.backend.context;

import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

public class RequestContext {
  public ResponseSpec response;
  public Object requestBody;

  public <T> T getRequestBodyAs(Class<T> clazz) {
    return (T) requestBody;
  }
}

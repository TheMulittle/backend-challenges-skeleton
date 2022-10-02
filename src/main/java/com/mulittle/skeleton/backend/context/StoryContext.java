package com.mulittle.skeleton.backend.context;

import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@Component
public class StoryContext {
  public ResponseSpec response;
  public Object requestBody;

  public <T> T getRequestBodyAs(Class<T> clazz) {
    return (T) requestBody;
  }
}

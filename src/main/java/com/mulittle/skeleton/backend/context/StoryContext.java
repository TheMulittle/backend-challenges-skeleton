package com.mulittle.skeleton.backend.context;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class StoryContext {
  public ResponseSpec response;
  public Object requestBody;

  public <T> T getRequestBodyAs(Class<T> clazz) {
    return (T) requestBody;
  }
}

package com.mulittle.skeleton.backend;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration 
@ComponentScan("com.mulittle.skeleton.backend")
public class SpringConfiguration {
  @Bean
  @Scope(value = SCOPE_CUCUMBER_GLUE, proxyMode = ScopedProxyMode.TARGET_CLASS)
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean
  @Scope(value = SCOPE_CUCUMBER_GLUE, proxyMode = ScopedProxyMode.TARGET_CLASS)
  public Map<String, Object> context() {
    return new HashMap<>();
  }
}
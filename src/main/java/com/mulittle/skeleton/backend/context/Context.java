package com.mulittle.skeleton.backend.context;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Scope(value = SCOPE_CUCUMBER_GLUE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Context {

  private static final String KEY_NOT_FOUND = "[%s] key was not found in context";

  private static final String CANNOT_CAST_TO_TYPE_MESSAGE = "Cannot get [%s] as type [%s]. The actual type is [%s]";

  private final Map<String, Object> items;

  private final ObjectMapper mapper;

  @Autowired
  public Context(Map<String, Object> contextItems, ObjectMapper mapper) {
    this.items = contextItems;
    this.mapper = mapper;
  }

  public <T> T findAs(String key, Class<T> clazz) {

    Object object = find(key);

    if(!clazz.isInstance(object)) {
      throw new ClassCastException(CANNOT_CAST_TO_TYPE_MESSAGE.formatted(key, clazz, object.getClass()));
    }

    return mapper.convertValue(object, clazz);
  }

  public Object find(String key) {
    if(!items.containsKey(key)) {
      throw new IllegalArgumentException(KEY_NOT_FOUND.formatted(key));
    }

    Object object = items.get(key);

    return object;
  }

  public void put(String key, Object object) {
    items.put(key, object);
  }

}

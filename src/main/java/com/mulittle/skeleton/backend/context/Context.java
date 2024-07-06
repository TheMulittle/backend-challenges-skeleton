package com.mulittle.skeleton.backend.context;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Context {

  
  private static final String KEY_NOT_FOUND = "[%s] key was not found in context";

  private static final String CANNOT_CAST_TO_TYPE_MESSAGE = "Cannot get [%s] as type [%s]. The actual type is [%s]";

  private final Map<String, Object> items;

  private final ObjectMapper mapper;

  public Context(Map<String, Object> contextItems, ObjectMapper mapper) {
    this.items = contextItems;
    this.mapper = mapper;
  }

  public <T> T findAs(String key, Class<T> clazz) {
    if(!items.containsKey(key)) {
      throw new IllegalArgumentException(KEY_NOT_FOUND.formatted(key));
    }

    Object object = items.get(key);

    if(!clazz.isInstance(object)) {
      throw new ClassCastException(CANNOT_CAST_TO_TYPE_MESSAGE.formatted(key, clazz, object.getClass()));
    }

    return mapper.convertValue(object, clazz);
  }

  public void put(String key, Object object) {
    items.put(key, object);
  }

}

package com.mulittle.skeleton.backend.context;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;


@Component
@Scope(value = SCOPE_CUCUMBER_GLUE, proxyMode = ScopedProxyMode.TARGET_CLASS)
@RequiredArgsConstructor
public class PlaceholderContext {

  private static final String INVALID_PATH_NOTATION = 
  """
  Context path does not follow path notation [objectKey].[attributeKey]
  The actual value is: %s
  """;

  private static final String OBJECT_WITH_KEY_NOT_FOUND = 
  """
  Object for key [ %s ] does not exist in the Placeholder Context
  Context keys: [ %s ]
  """;

  private static final String ATTRIBUTE_WITH_KEY_NOT_FOUND =     
  """
  Attribute for key [ %s ] does not exist in the Placeholder Context for object with key [ %s ]
  Attribute keys: [ %s ]
  """;

  private static final String CANNOT_CAST_TO_TYPE_MESSAGE = "Cannot get '%s' as type '%s'. The actual type is '%s'";

  private final Map<String, Object> context;

  private final ObjectMapper mapper; 

  public void put(final String key, final Object value) {
    context.put(key, value);
  }

  public <V> V getAttributeAs(String path, Class<V> attributeValueClass) {
    String[] keys = path.split("\\.");

    if(keys.length != 2) {
      throw new IllegalArgumentException(INVALID_PATH_NOTATION.formatted(path));
    }

    return  getAttributeAs(keys[0], keys[1], attributeValueClass);
  }

  public <V> V getAttributeAs(String objectKey, String attributeKey, Class<V> attributeValueClass) {
    objectExists(objectKey);

    Object object = context.get(objectKey);
    TypeReference<Map<String, Object>> type = new TypeReference<Map<String, Object>>() { };
    Map<String, Object> objectAsMap = mapper.convertValue(object, type);
    attributeExists(objectKey, attributeKey, objectAsMap);
    
    Object attributeValue = objectAsMap.get(attributeKey);
    String message = CANNOT_CAST_TO_TYPE_MESSAGE.formatted(objectKey + "." + attributeKey, attributeValueClass, attributeValue.getClass());
    isObjectOfType(message, attributeValue, attributeValueClass);

    return  attributeValueClass.cast(attributeValue);
  }

  public <V> V getObjectAs(String objectKey, Class<V> objectClass) {
    objectExists(objectKey);

    Object object = context.get(objectKey);
    String message = CANNOT_CAST_TO_TYPE_MESSAGE.formatted(objectKey, object.getClass(), objectClass.getClass());
    isObjectOfType(message, object, objectClass);

    return  objectClass.cast(object);
  }

 private <V> void isObjectOfType(String message, Object object, Class<V> objectClass) {
    if(!objectClass.isInstance(object)) {
      throw new ClassCastException(message);
    }
  }

  private void attributeExists(String objectKey, String attributeKey, Map<String, Object> object) {
    if(!object.containsKey(attributeKey)) {
      throw new IllegalArgumentException(ATTRIBUTE_WITH_KEY_NOT_FOUND.formatted(attributeKey, objectKey, object.keySet()));
    }
  }

  private void objectExists(String objectKey) {
    if(!context.containsKey(objectKey)) {
      throw new IllegalArgumentException(OBJECT_WITH_KEY_NOT_FOUND.formatted(objectKey, context.keySet()));
    }
  }
}

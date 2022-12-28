package com.mulittle.skeleton.backend.context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@TestInstance(Lifecycle.PER_METHOD)
@ExtendWith(MockitoExtension.class)
public class PlaceholderContextTest {

  private static final String OBJECT_KEY = "object";

  private static final String ATTRIBUTE_KEY = "attribute";

  @Mock
  Map<String, Object> contextMock;

  @Mock
  Object attributeMock;

  @Mock
  ObjectMapper mapperMock;

  @Mock
  Map<String, Object> mapMock;

  @Mock
  Object objectMock;

  PlaceholderContext placeholderContext;

  @BeforeEach
  public void setup() {
    placeholderContext = new PlaceholderContext(contextMock, mapperMock);
  }

  @Test
  @DisplayName("PlaceholderContext#getAttributeAs returns attribute cast to type")
  void get_returns_attribute_cast_to_type() {
    // Arrange
    when(contextMock.get(OBJECT_KEY)).thenReturn(attributeMock);
    when(contextMock.containsKey(OBJECT_KEY)).thenReturn(true);

    when(mapperMock.convertValue(any(), any(TypeReference.class))).thenReturn(mapMock);
    when(mapMock.get(ATTRIBUTE_KEY)).thenReturn(mapMock);
    when(mapMock.containsKey(ATTRIBUTE_KEY)).thenReturn(true);

    //Act
    Map result = placeholderContext.getAttributeAs(OBJECT_KEY + "." + ATTRIBUTE_KEY, Map.class);
    assertEquals(result, mapMock);
  }

  @Test
  @DisplayName("PlaceholderContext#getAttributeAs returns object cast to type")
  void get_returns_object_cast_to_type() {
    // Arrange
    when(contextMock.get(OBJECT_KEY)).thenReturn(mapMock);
    when(contextMock.containsKey(OBJECT_KEY)).thenReturn(true);

    //Act
    Map result = placeholderContext.getObjectAs(OBJECT_KEY, Map.class);
    assertEquals(result, mapMock);
  }

  @Test
  @DisplayName("PlaceholderContext#getAttributeAs throws exception when the requested attribute cannot be cast to the Type")
  void get_throws_exception_when_attribute_cannot_be_cast() {
    
    // Arrange
    when(mapperMock.convertValue(any(), any(TypeReference.class))).thenReturn(mapMock);
    when(contextMock.get(OBJECT_KEY)).thenReturn(attributeMock);
    when(contextMock.containsKey(OBJECT_KEY)).thenReturn(true);
    when(mapMock.get(ATTRIBUTE_KEY)).thenReturn(objectMock);
    when(mapMock.containsKey(ATTRIBUTE_KEY)).thenReturn(true);

    String message = "Cannot get 'object.attribute' as type 'class java.lang.Class'. The actual type is 'class java.lang.Object'";
    // Act - Assert
    Assertions.assertThatThrownBy(() -> placeholderContext.getAttributeAs(OBJECT_KEY, ATTRIBUTE_KEY, Class.class))
      .isInstanceOf(ClassCastException.class)
      .hasMessageContaining(message);
  }

  @Test
  @DisplayName("PlaceholderContext#getAttributeAs throws exception when no object exists for the key")
  void get_throws_exception_when_key_does_not_exist() {
    // Arrange
    when(contextMock.containsKey(OBJECT_KEY)).thenReturn(false);

    String message = "Object for key [ " + OBJECT_KEY + " ] does not exist in the Placeholder Context\nContext keys: [ [] ]";
    
    // Act - Assert
    Assertions.assertThatThrownBy(() -> placeholderContext.getAttributeAs(OBJECT_KEY, ATTRIBUTE_KEY, Class.class))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessageContaining(message);
  }

  @Test
  @DisplayName("PlaceholderContext#getAttributeAs throws exception when the given attribute key does not exist in the object")
  void get_throws_exception_when_attribute_is_null() {
    when(contextMock.get(OBJECT_KEY)).thenReturn(attributeMock);
    when(contextMock.containsKey(OBJECT_KEY)).thenReturn(true);
    
    when(mapperMock.convertValue(any(), any(TypeReference.class))).thenReturn(mapMock);
    when(mapMock.containsKey(ATTRIBUTE_KEY)).thenReturn(false);

    String message = "Attribute for key [ " + ATTRIBUTE_KEY + " ] does not exist in the Placeholder Context for object with key [ " + OBJECT_KEY + " ]"
    + "\nAttribute keys: [ [] ]";
    
    // Act - Assert
    Assertions.assertThatThrownBy(() -> placeholderContext.getAttributeAs(OBJECT_KEY, ATTRIBUTE_KEY, Class.class))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessageContaining(message);
  }

  @Test
  @DisplayName("PlaceholderContext#getAttributeAs can be called with [objectKey].[attributeKey] notation")
  void get_can_be_called_with_notation() {
    // Arrange
    when(contextMock.get(OBJECT_KEY)).thenReturn(mapMock);
    when(contextMock.containsKey(OBJECT_KEY)).thenReturn(true);
    when(mapMock.get(ATTRIBUTE_KEY)).thenReturn((Object) mapMock);
    when(mapMock.containsKey(ATTRIBUTE_KEY)).thenReturn(true);
    when(mapperMock.convertValue(any(), any(TypeReference.class))).thenReturn(mapMock);

    //Act
    Map result = placeholderContext.getAttributeAs(OBJECT_KEY + "." + ATTRIBUTE_KEY, Map.class);
    assertEquals(result, mapMock);
  }

  @ParameterizedTest(name = "PlaceholderContext#getAttributeAs throws exception when using one argument signature with wrong notation")
  @ValueSource(strings = {
      "objectKey",
      "objectKey,attributeKey",
      "objectKey.attributeKey.otherKey"
  })
  void get_throws_exception_when_notation_is_wrong(String keyPath) {
    // Arrange
    String message = "Context path does not follow path notation [objectKey].[attributeKey]\nThe actual value is: "
        + keyPath;

    // Act - Assert
    Assertions.assertThatThrownBy(() -> placeholderContext.getAttributeAs(keyPath, Class.class))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(message);
  }

  @Test
  @DisplayName("PlaceholderContext#put calls underlying Map#map exactly once")
  void put_calls_underlying_map_put() {
    // Arrange

    // Act
    placeholderContext.put(OBJECT_KEY, objectMock);

    // Assert
    verify(contextMock, times(1)).put(OBJECT_KEY, objectMock);
  }

}

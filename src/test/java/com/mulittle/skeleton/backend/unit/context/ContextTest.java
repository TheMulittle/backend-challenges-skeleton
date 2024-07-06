package com.mulittle.skeleton.backend.unit.context;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mulittle.skeleton.backend.context.Context;

@TestInstance(Lifecycle.PER_METHOD)
@ExtendWith(MockitoExtension.class)
public class ContextTest {

  private static final String OBJECT_KEY = "object";

  private static final String OBJECT_VALUE = "value";

  @Mock
  Map<String, Object> contextItemsMock;

  @Mock
  ObjectMapper mapperMock;

  @Mock
  Map<String, Object> mapMock;

  @Mock
  Object objectMock;

  Context context;

  @BeforeEach
  public void setup() {
    context = new Context(contextItemsMock, mapperMock);
  }

  @Test
  @DisplayName("Context#findAs returns object cast to type")
  void find_as_returns_attribute_cast_to_type() {
    // Arrange
    when(contextItemsMock.containsKey(OBJECT_KEY)).thenReturn(true);
    when(contextItemsMock.get(OBJECT_KEY)).thenReturn((Object) mapMock);


    when(mapperMock.convertValue((Object) mapMock, Map.class)).thenReturn(mapMock);

    //Act
    Map<String, Object> result = context.findAs(OBJECT_KEY, Map.class);
    assertEquals(result, mapMock);
  }

  @Test
  @DisplayName("Context#findAs throws exception when the object cannot be cast to the Type")
  void find_as_throws_exception_when_object_cannot_be_cast() {
    // Arrange
    when(contextItemsMock.containsKey(OBJECT_KEY)).thenReturn(true);
    when(contextItemsMock.get(OBJECT_KEY)).thenReturn(objectMock);

    //Act
    //Assert
    Assertions.assertThatThrownBy(() -> context.findAs(OBJECT_KEY, Map.class))
    .isInstanceOf(ClassCastException.class)
    .hasMessageContaining("Cannot get [object] as type [interface java.util.Map]. The actual type is [class java.lang.Object]");
  }

  @Test
  @DisplayName("Context#findAs throws exception when the object with key does not exist")
  void get_throws_exception_when_attribute_cannot_be_cast() {
    // Arrange
    when(contextItemsMock.containsKey(OBJECT_KEY)).thenReturn(false);

    //Act
    //Assert
    Assertions.assertThatThrownBy(() -> context.findAs(OBJECT_KEY, Map.class))
    .isInstanceOf(IllegalArgumentException.class)
    .hasMessageContaining("[object] key was not found in context");

  }

  @Test
  @DisplayName("Context#put adds item to contet")
  void put_adds_item_to_context() {
    // Arrange

    // Act
    context.put(OBJECT_KEY, OBJECT_VALUE);

    // Assert
    verify(contextItemsMock).put(OBJECT_KEY, OBJECT_VALUE);
  }
}

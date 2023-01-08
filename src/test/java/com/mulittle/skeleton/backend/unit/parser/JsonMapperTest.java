package com.mulittle.skeleton.backend.unit.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mulittle.skeleton.backend.parser.JsonMapper;

public class JsonMapperTest {

  @Test
  public void mapWithoutEllipsis() throws JsonMappingException, JsonProcessingException {
    // Arrange
    String json = "{\"name\": \"abc\"}";
    Map<String, Object> expected = new HashMap<>();
    expected.put("name", "abc");

    // Act
    Map<String, Object> actual = JsonMapper.jsonStringToMap(json);

    // Assert
    assertThat(actual, is(expected));
  }
}

package com.mulittle.skeleton.backend.parser;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMapper {

  public static Map<String, Object> jsonStringToMap(String jsonString) throws JsonMappingException, JsonProcessingException {
    TypeReference<Map<String,Object>> typeRef = new TypeReference<Map<String,Object>>() {};

    return new ObjectMapper().readValue(jsonString, typeRef);
  }
}

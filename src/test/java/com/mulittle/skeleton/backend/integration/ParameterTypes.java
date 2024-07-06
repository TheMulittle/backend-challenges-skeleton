package com.mulittle.skeleton.backend.integration;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mulittle.skeleton.backend.context.PlaceholderContext;
import com.mulittle.skeleton.backend.parser.ContextAwarePlaceholderReplacer;
import com.mulittle.skeleton.backend.parser.JsonMapper;

import io.cucumber.java.DocStringType;
import io.cucumber.java.ParameterType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ParameterTypes {
  
  private final PlaceholderContext placeholderContext;
    
   @DocStringType
 
    public Map<String, Object> json(String docString) throws JsonMappingException, JsonProcessingException {
        //String body = ContextAwarePlaceholderReplacer.replace(docString, placeholderContext);
        return JsonMapper.jsonStringToMap(docString);
    }

    @ParameterType("(.*)")
    public String endpoint(String endpoint) throws JsonMappingException, JsonProcessingException {
        //String body = ContextAwarePlaceholderReplacer.replace(endpoint, placeholderContext);
        return endpoint;
    }

}

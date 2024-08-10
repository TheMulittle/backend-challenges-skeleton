package com.mulittle.skeleton.backend.parser;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;

public class JsonAssertions {

  @SuppressWarnings("unchecked")
  public static void assertJsonsMatch(String actualJson, String expectedJson) {
    Map<String, Object> actualJsonAsMap = new GsonBuilder()
    .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
    .create()
    .fromJson(actualJson, Map.class);
    Map<String, Object> expectedJsonAsMap = new GsonBuilder()
    .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
    .create()
    .fromJson(expectedJson, Map.class);

    new MapAssertions().assertThat(actualJsonAsMap).withStringAsserter(new ContextAwareStringAsserter(null)).matches(expectedJsonAsMap);

    //UmaClasseQualquer.fazerAlgo(actualJsonAsMap, expectedJsonAsMap) -> go through every map entry
    // and check they match. If they have P character then it will call special code
    //MapNodeOperator.operate()

    //new Gson().fromJson(expected, Map.class);

  }

}

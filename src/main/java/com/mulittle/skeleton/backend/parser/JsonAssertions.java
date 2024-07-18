package com.mulittle.skeleton.backend.parser;

import java.util.Map;

import org.assertj.core.api.Assertions;
import org.springframework.boot.json.GsonJsonParser;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

public class JsonAssertions {

  public static void assertJsonsMatch(String actualJson, String expectedJson) {
    Map<String, Object> actualJsonAsMap = new Gson().fromJson(actualJson, Map.class);
    Map<String, Object> expectedJsonAsMap = new Gson().fromJson(expectedJson, Map.class);

    //UmaClasseQualquer.fazerAlgo(actualJsonAsMap, expectedJsonAsMap) -> go through every map entry
    // and check they match. If they have P character then it will call special code
    //MapNodeOperator.operate()

    //new Gson().fromJson(expected, Map.class);
  }

}

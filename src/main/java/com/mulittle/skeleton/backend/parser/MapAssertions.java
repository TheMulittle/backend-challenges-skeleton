package com.mulittle.skeleton.backend.parser;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MapAssertions {

  private static final String KEY_NOT_PRESENT_REASON = "key [%s] from the expected map does not exist in actual map";

  private final List<AssertionDifference> assertionsDifferences;

  private Map<String, ?> actualMap;

  private StringBuilder currentPath;

  public MapAssertions() {
    this.assertionsDifferences = new LinkedList<>();
    this.currentPath = new StringBuilder();
    currentPath.append("$");
  }

  public MapAssertions assertThat(Map<String, ?> actualMap) {
    this.actualMap = actualMap;
    return this;
  }

  public void matches(Map<String, ?> expectedMap) {
    assertMap(expectedMap);
    throwIfAssertionFailed();   
  }

  private void assertMap(Map<String, ?> expectedMap) {
    expectedMap.entrySet().forEach(expectedEntry -> {

        if(!actualMap.containsKey(expectedEntry.getKey())) {
          AssertionDifference assertionDifference = new AssertionDifference(currentPath.toString(), actualMap, expectedMap, KEY_NOT_PRESENT_REASON.formatted(expectedEntry.getKey()));
          assertionsDifferences.add(assertionDifference);
        }

        if(expectedEntry.getValue() instanceof Integer value) {
          assertInteger(actualMap.get(expectedEntry.getKey()), value);
        }


    });    
  }

  private void assertInteger(Object actualValue, Integer expectedValue) {
    /*if(!expectedValue.equals(actualValue)) {
      AssertionError x = new AssertionError("""
          joao
          """);
      assertionsErrors.add(x);
    }*/
  }

  
  private void throwIfAssertionFailed() {
    if(!assertionsDifferences.isEmpty()) {
      StringBuilder message = new StringBuilder();
      message.append("Expected map and actual map do not match. Differences are:\n\n");
      assertionsDifferences.forEach(assertionDifference -> {
        message.append(assertionDifference.getMessage());
      });
      throw new AssertionError(message.toString());
    }
  }
}

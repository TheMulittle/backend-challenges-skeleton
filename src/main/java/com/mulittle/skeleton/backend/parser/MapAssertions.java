package com.mulittle.skeleton.backend.parser;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class MapAssertions {

  private static final String KEY_NOT_PRESENT_REASON = "key [%s] from the expected map does not exist in actual map";

  private static final String MAPS_OF_DIFFERENT_SIZE = "actual and expected maps have different sizes, actual size is [%s] and expected size is [%s]";

  private static final String NOT_A_LIST = "expected field is a List but actual field is [%s]";

  private static final String NOT_A_MAP = "expected field is a Map but actual field is [%s]";

  private static final String LIST_OF_DIFFERENT_SIZES = "actual and expected lists have different sizes, actual size is [%s] and expected size is [%s]";

  private final List<AssertionDifference> assertionsDifferences;

  private Map<String, ?> actualMap;

  private StringAsserter stringAsserter;

  public MapAssertions() {
    this.assertionsDifferences = new LinkedList<>();
    this.stringAsserter = new DefaultStringAsserter();
  }

  public MapAssertions(StringAsserter stringAsserter) {
    this.assertionsDifferences = new LinkedList<>();
    this.stringAsserter = stringAsserter;
  }

  public MapAssertions withStringAsserter(StringAsserter stringAsserter) {
    this.stringAsserter = stringAsserter;
    return this;
  }

  public MapAssertions assertThat(Map<String, ?> actualMap) {
    this.actualMap = actualMap;
    return this;
  }

  public void matches(Map<String, ?> expectedMap) {
    assertMap("$", actualMap, expectedMap);
    throwIfAssertionFailed();   
  }

  private void assertMap(String currentPath, Map<String, ?> actualMap, Map<String, ?> expectedMap) {

    if(actualMap.size() != expectedMap.size()) {
      AssertionDifference assertionDifference = new AssertionDifference(currentPath, actualMap, expectedMap, MAPS_OF_DIFFERENT_SIZE.formatted(actualMap.size(), expectedMap.size()));
      assertionsDifferences.add(assertionDifference);
      return;
    }

    expectedMap.entrySet().forEach(expectedEntry -> {

        if(!actualMap.containsKey(expectedEntry.getKey())) {
          AssertionDifference assertionDifference = new AssertionDifference(currentPath, actualMap, expectedMap, KEY_NOT_PRESENT_REASON.formatted(expectedEntry.getKey()));
          assertionsDifferences.add(assertionDifference);
          return;
        }

        Object expectedEntryValue = expectedEntry.getValue();
        String expectedEntryKey = expectedEntry.getKey();
        Object actualEntryValue = actualMap.get(expectedEntryKey);

        assertEntry(currentPath, expectedEntryKey, actualEntryValue, expectedEntryValue);


    });    
  }

  private void assertEntry(String currentPath, String extraPath, Object actual, Object expected) {
    final String newPath = currentPath.concat(".").concat(extraPath);

    if (expected == null) {
      assertNull(newPath, actual, expected);
      return;
    }

    if (expected instanceof List value) {
      assertList(newPath, actual, value);
      return;
    }

    if(expected instanceof Map value) {
      if(!(actual instanceof Map)) {
        String message = actual != null ? NOT_A_MAP.formatted(actual.getClass()) : NOT_A_MAP.formatted(null);
        AssertionDifference asssertionDifference = new AssertionDifference(newPath, actual, value, message);
        assertionsDifferences.add(asssertionDifference);
        return;
      }
      assertMap(newPath, (Map) actual, (Map) value);
      return;
    }
    
    if(expected instanceof String value) {
      assertString(newPath, actual, value);
      return;
    }

    assertOthers(newPath, actual, expected);
    return;
  }

  private void assertNull(String newPath, Object actual, Object expected) {
    if (actual != null) {
      AssertionDifference asssertionDifference = new AssertionDifference(newPath, actual, expected);
      assertionsDifferences.add(asssertionDifference);
      return;
    }

  }

  private void assertList(final String newPath, Object actual, List value) {
    if(!(actual instanceof List)) {
      String message = actual != null ? NOT_A_LIST.formatted(actual.getClass()) : NOT_A_LIST.formatted(null);
      AssertionDifference asssertionDifference = new AssertionDifference(newPath, actual, value, message);
      assertionsDifferences.add(asssertionDifference);
      return;
    }

    if(((List) actual).size() != value.size()) {
      String message = LIST_OF_DIFFERENT_SIZES.formatted(((List) actual).size(), value.size());
      AssertionDifference asssertionDifference = new AssertionDifference(newPath, actual, value, message);
      assertionsDifferences.add(asssertionDifference);
      return;
    }

    IntStream.range(0, value.size())
      .forEach(idx -> {
        assertEntry(newPath, "[%d]".formatted(idx), ((List) actual).get(idx), value.get(idx));
      }
    );
  }

  private void assertString(String path, Object actualValue, String expectedValue) {
      Optional<AssertionDifference> assertionDifference = stringAsserter.assertString(actualValue, expectedValue);

      assertionDifference.ifPresent((value) -> {
        value.setPath(path);
        assertionsDifferences.add(value);
      });
  }

  private void assertOthers(String path, Object actualValue, Object expectedValue) {
    if(!expectedValue.equals(actualValue)) {
      AssertionDifference asssertionDifference = new AssertionDifference(path, actualValue, expectedValue);
      assertionsDifferences.add(asssertionDifference);
    }
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

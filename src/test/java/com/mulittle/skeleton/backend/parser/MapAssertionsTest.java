package com.mulittle.skeleton.backend.parser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings({"rawtypes", "unchecked"})
public class MapAssertionsTest {
  
  @Test
  @DisplayName("MapAssertions#matches does not throw error when maps match")
  void testAssertMapsMatch() {
    // Arranje
    Map<String, Object> actual = new HashMap();
    Map<String, Object> actualMapInsideList = new HashMap();
    actualMapInsideList.put("innerKey", 5);
    List<Object> actualEmptyList = new LinkedList<>();
    List<Object> actualList = new LinkedList<>();
    actualList.add(true);
    actualList.add(5);
    actualList.add(5.0f);
    actualList.add(true);
    actual.put("emptyList", actualEmptyList);
    actual.put("list", actualEmptyList);
    actual.put("integer", 5);
    actual.put("float", 5.0f);
    actual.put("boolean", true);

    Map<String, Object> expected = new HashMap();
    Map<String, Object> expectedMapInsideList = new HashMap();
    expectedMapInsideList.put("innerKey", 5);
    List<Object> expectedEmptyList = new LinkedList<>();
    List<Object> expectedList = new LinkedList<>();
    expectedList.add(true);
    expectedList.add(5);
    expectedList.add(5.0f);
    expectedList.add(true);
    expected.put("emptyList", expectedEmptyList);
    expected.put("list", expectedEmptyList);
    expected.put("integer", 5);
    expected.put("float", 5.0f);
    expected.put("boolean", true);

    // Act 
    // Assert
    Assertions.assertThatNoException()
        .isThrownBy(() -> new MapAssertions().assertThat(actual).matches(expected));

  }

  @Test
  @DisplayName("MapAssertions#matches throws multiples assertions due to its soft nature")
  void thorws_multiple_assertions_failures() {
    // Arranje
    Map<String, Integer> actual = new HashMap();
    actual.put("key", 5);
    actual.put("anotherKey", 10);

    Map<String, Integer> expected = new HashMap();
    expected.put("key", 45);
    expected.put("anotherKey", 45);

    // Act
    // Assert
    Assertions.assertThatThrownBy(() -> new MapAssertions().assertThat(actual).matches(expected))
        .isInstanceOf(AssertionError.class)
        .hasMessage(
            """
                Expected map and actual map do not match. Differences are:

                field with path '$.anotherKey' differ:
                - actual value  : 10
                - expected value: 45

                field with path '$.key' differ:
                - actual value  : 5
                - expected value: 45

                """);
  }

  @Test
  @DisplayName("MapAssertions#matches throws error when maps have different sizes")
  void throws_error_when_maps_have_different_sizes() {
    // Arranje
    Map<String, Integer> actual = new HashMap();
    actual.put("key", 5);
    actual.put("anotherKey", 10);

    Map<String, Integer> expected = new HashMap();
    expected.put("key", 5);

    // Act
    // Assert
    Assertions.assertThatThrownBy(() -> new MapAssertions().assertThat(actual).matches(expected))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining(
            """
                field with path '$' differ:
                - actual value  : {anotherKey=10, key=5}
                - expected value: {key=5}
                actual and expected maps have different sizes, actual size is [2] and expected size is [1]
                """);
  }

  @Test
  @DisplayName("MapAssertions#assertMapsMatch throws error when inner maps have different sizes")
  void throws_error_when_inner_maps_have_different_sizes() {
    // Arranje
    Map<String, Object> actual = new HashMap();
    Map<String, Object> innerActual = new HashMap();
    actual.put("key", innerActual);

    Map<String, Object> expected = new HashMap();
    Map<String, Object> innerExpected = new HashMap();
    innerExpected.put("innerKey", "5");
    expected.put("key", innerExpected);

    // Act
    // Assert
    Assertions.assertThatThrownBy(() -> new MapAssertions().assertThat(actual).matches(expected))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining(
            """
                field with path '$.key' differ:
                - actual value  : {}
                - expected value: {innerKey=5}
                actual and expected maps have different sizes, actual size is [0] and expected size is [1]
                """);
  }

  @Test
  @DisplayName("MapAssertions#matches throws assertion error when an expected map key does not exist in actual map")
  void throws_error_when_expected_key_does_not_exist_in_actual_map() {
    // Arranje
    Map<String, Integer> actual = new HashMap();
    actual.put("id", 5);

    Map<String, Integer> expected = new HashMap();
    expected.put("idx", 5);

    // Act
    // Assert
    Assertions.assertThatThrownBy(() -> new MapAssertions().assertThat(actual).matches(expected))
      .isInstanceOf(AssertionError.class)
        .hasMessageContaining(
        """
                field with path '$' differ:
                - actual value  : {id=5}
                - expected value: {idx=5}
                key [idx] from the expected map does not exist in actual map
                """);
  }

  @Test
  @DisplayName("MapAssertions#assertMapsMatch throws error when actual property is String and expected property is Integer")
  void throws_error_when_expected_value_is_integer_and_actual_value_is_string() {
    // Arranje
    Map<String, String> actual = new HashMap();
    actual.put("key", "5");

    Map<String, Integer> expected = new HashMap();
    expected.put("key", 5);

    // Act
    // Assert
    Assertions.assertThatThrownBy(() -> new MapAssertions().assertThat(actual).matches(expected))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining(
            """
                field with path '$.key' differ:
                - actual value  : "5"
                - expected value: 5
                """);
  }

  @Test
  @DisplayName("MapAssertions#assertMapsMatch throws error when Integer values do not match")
  void throws_error_when_expected_and_actual_are_integers_that_do_not_match() {
    // Arranje
    Map<String, Integer> actual = new HashMap();
    actual.put("key", 5);

    Map<String, Integer> expected = new HashMap();
    expected.put("key", 4);

    // Act
    // Assert
    Assertions.assertThatThrownBy(() -> new MapAssertions().assertThat(actual).matches(expected))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining("""
            field with path '$.key' differ:
            - actual value  : 5
            - expected value: 4
            """);
  }

  @Test
  @DisplayName("MapAssertions#matches throws error when actual property is Integer and expected property is String")
  void throws_error_when_expected_value_is_string_and_actual_value_is_integer() {
    // Arranje
    Map<String, Integer> actual = new HashMap();
    actual.put("key", 5);

    Map<String, String> expected = new HashMap();
    expected.put("key", "5");

    // Act
    // Assert
    Assertions.assertThatThrownBy(() -> new MapAssertions().assertThat(actual).matches(expected))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining(
            """
                field with path '$.key' differ:
                - actual value  : 5
                - expected value: "5"
                """);
  }

  @Test
  @DisplayName("MapAssertions#matches throws error when actual property is Integer and expected property is Float")
  void throws_error_when_actual_value_is_integer_and_expected_value_is_float() {
    // Arranje
    Map<String, Integer> actual = new HashMap();
    actual.put("key", 5);

    Map<String, Float> expected = new HashMap();
    expected.put("key", 5.0f);

    // Act
    // Assert
    Assertions.assertThatThrownBy(() -> new MapAssertions().assertThat(actual).matches(expected))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining(
            """
            field with path '$.key' differ:
            - actual value  : 5
            - expected value: 5.0
            """);
  }

  @Test
  @DisplayName("MapAssertions#matches throws error when actual property is List and expected property is Map")
  void throws_error_when_actual_is_list_and_expected_is_map() {
    // Arranje
    Map<String, List<Integer>>actual = new HashMap();
    List<Integer> actualInnerList = new LinkedList<>();
    actualInnerList.add(5);

    actual.put("key", actualInnerList);

    Map<String, Map<String, Object>> expected = new HashMap();
    Map<String, Object> expectedInnerMap = new HashMap();
    expectedInnerMap.put("innerKey", 5);
    expected.put("key", expectedInnerMap);
    
    // Act
    // Assert
    Assertions.assertThatThrownBy(() -> new MapAssertions().assertThat(actual).matches(expected))
    .isInstanceOf(AssertionError.class)
    .hasMessageContaining(
    """
    field with path '$.key' differ:
    - actual value  : [5]
    - expected value: {innerKey=5}
    expected field is a Map but actual field is [class java.util.LinkedList]
    """
    );
  }

  @Test
  @DisplayName("MapAssertions#matches throws error when actual property is Map and expected property is List")
  void throws_error_when_actual_is_map_and_expected_is_list() {
    // Arranje
    Map<String, Map<String, Object>> actual = new HashMap();
    Map<String, Object> actualInnerMap = new HashMap();
    actualInnerMap.put("innerKey", 5);
    actual.put("key", actualInnerMap);

    Map<String, List<Integer>> expected = new HashMap();
    List<Integer> expectedInnerList = new LinkedList<>();
    expectedInnerList.add(5);
    expected.put("key", expectedInnerList);

    // Act
    // Assert
    Assertions.assertThatThrownBy(() -> new MapAssertions().assertThat(actual).matches(expected))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining(
            """
                field with path '$.key' differ:
                - actual value  : {innerKey=5}
                - expected value: [5]
                expected field is a List but actual field is [class java.util.HashMap]
                """);
  }

  @Test
  @DisplayName("MapAssertions#matches throws error when actual and expected properties are lists of different sizes")
  void throws_error_when_actual_and_expected_are_lists_of_different_sizes() {
    // Arranje
    Map<String, List<Integer>> actual = new HashMap();
    List<Integer> actualList = new LinkedList();
    actual.put("key", actualList);

    Map<String, List<Integer>> expected = new HashMap();
    List<Integer> expectedList = new LinkedList();
    expectedList.add(5);
    expected.put("key", expectedList);

    // Act
    // Assert
    Assertions.assertThatThrownBy(() -> new MapAssertions().assertThat(actual).matches(expected))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining(
            """
            field with path '$.key' differ:
            - actual value  : []
            - expected value: [5]
            actual and expected lists have different sizes, actual size is [0] and expected size is [1]
            """);
  }

  @Test
  @DisplayName("MapAssertions#matches throws error when actual and expected properties are lists with different items")
  void throws_error_when_actual_and_expected_are_lists_containing_different_items() {
    // Arranje
    Map<String, List<Integer>> actual = new HashMap();
    List<Integer> actualList = new LinkedList();
    actualList.add(4);
    actual.put("key", actualList);

    Map<String, List<Integer>> expected = new HashMap();
    List<Integer> expectedList = new LinkedList();
    expectedList.add(5);
    expected.put("key", expectedList);

    // Act
    // Assert
    Assertions.assertThatThrownBy(() -> new MapAssertions().assertThat(actual).matches(expected))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining(
            """
            field with path '$.key.[0]' differ:
            - actual value  : 4
            - expected value: 5
            """);
  }
}

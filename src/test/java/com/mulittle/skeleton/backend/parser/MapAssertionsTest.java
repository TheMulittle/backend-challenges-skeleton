package com.mulittle.skeleton.backend.parser;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

public class MapAssertionsTest {

  @Test
  @DisplayName("MapAssertions#assertMapsMatch does not throw error when maps match")
  void testAssertMapsMatch() {

  }

  @Test
  @DisplayName("MapAssertions#assertMapsMatch throws assertion error when an expected map key does not exist in actual map")
  void c() {
    // Arranje
    Map<String, Integer> actual = new HashMap();
    actual.put("id", 5);

    Map<String, Integer> expected = new HashMap();
    expected.put("idx",5);

    // Act
    // Assert
    Assertions.assertThatThrownBy(() -> new MapAssertions().assertThat(actual).matches(expected))
      .isInstanceOf(AssertionError.class)
      .hasMessage(
        """
        Expected map and actual map do not match. Differences are:

        field with path '$' differ:
        - actual value  : {id=5}
        - expected value: {idx=5}
        key [idx] from the expected map does not exist in actual map  
        """
      );
  }

  /*@Test
  @DisplayName("MapAssertions#assertMapsMatch throws error when values with the same type do not match")
  void a() {
    // Arranje
    Map<String, Integer> actual = new HashMap();
    actual.put("id", 5);

    Map<String, Integer> expected = new HashMap();
    expected.put("id", 4);

    // Act
    // Assert
    Assertions.assertThatThrownBy(() -> MapAssertions.assertMapsMatch(actual, expected))
      .isInstanceOf(AssertionError.class)
      .hasMessage("null");
  }

  @Test
  @DisplayName("MapAssertions#assertMapsMatch throws error when values of different types do not match")
  void b() {
    // Arranje
    Map<String, Integer> actual = new HashMap();
    actual.put("id", 5);

    Map<String, String> expected = new HashMap();
    expected.put("id", "5");

    // Act
    // Assert
    Assertions.assertThatThrownBy(() -> MapAssertions.assertMapsMatch(actual, expected))
      .isInstanceOf(AssertionError.class)
      .hasMessage("null");
  }

  @Test
  @DisplayName("MapAssertions#assertMapsMatch throws multiples assertions due to its soft nature")
  void d() {
    // Arranje
    Map<String, Integer> actual = new HashMap();
    actual.put("key", 5);
    actual.put("anotherKey", 10);

    Map<String, Integer> expected = new HashMap();
    expected.put("key",45);
    expected.put("anotherKey",45);

    // Act
    // Assert
    Assertions.assertThatThrownBy(() -> new MapAssertions().assertThat(actual).assertMapsMatch(expected))
      .isInstanceOf(AssertionError.class)
      .hasMessage(
        """
          Expected and actual do not match. Differences are:

          field with path '$.id' differ:
          - actual value  : 5
          - expected value: 45 

          field with path '$.anotherKey' differ:
          - actual value  : 10
          - expected value: 45
        """
      );
  }

  @Test
  @DisplayName("MapAssertions#assertMapsMatch throws error when map has different sizes")
  void e() {
    // Arranje
    Map<String, Integer> actual = new HashMap();
    actual.put("key", 5);
    actual.put("anotherKey", 10);

    Map<String, Integer> expected = new HashMap();
    expected.put("key",5);

    // Act
    // Assert
    Assertions.assertThatThrownBy(() -> new MapAssertions().assertThat(actual).assertMapsMatch(expected))
      .isInstanceOf(AssertionError.class)
      .hasMessage(
        """
          Expected and actual maps do not match. Differences are:

          field with path '$' differ:
          - actual value  :
          - expected value:
          actual and expected maps have different sizes, actual size is [2] and expected size is [1]
        """
      );
  }

  @Test
  @DisplayName("MapAssertions#assertMapsMatch throws error when inner maps have different sizes")
  void e() {
    // Arranje
    Map<String, Object> actual = new HashMap();
    Map<String, Object> innerActual = new HashMap();
    actual.put("key", innerActual);

    Map<String, Object> expected = new HashMap();
    Map<String, Object> innerExpected = new HashMap();
    innerExpected.put("innerKey", "5");
    expected.put("key",innerExpected);

    // Act
    // Assert
    Assertions.assertThatThrownBy(() -> new MapAssertions().assertThat(actual).assertMapsMatch(expected))
      .isInstanceOf(AssertionError.class)
      .hasMessage(
        """
          Expected and actual maps do not match. Differences are:

          field with path '$.key' differ:
          - actual value  :
          - expected value:
          actual and expected have different sizes, actual size is [0] and expected size is [1]
        """
      );
  }

  @Test
  @DisplayName("MapAssertions#assertMapsMatch throws error when actual property is Integer and expected property is String")
  void f() {
    // Arranje
    Map<String, Object> actual = new HashMap();
    Map<String, Object> innerActual = new HashMap();
    actual.put("key", innerActual);

    Map<String, Object> expected = new HashMap();
    Map<String, Object> innerExpected = new HashMap();
    innerExpected.put("innerKey", "5");
    expected.put("key",innerExpected);

    // Act
    // Assert
    Assertions.assertThatThrownBy(() -> new MapAssertions().assertThat(actual).assertMapsMatch(expected))
      .isInstanceOf(AssertionError.class)
      .hasMessage(
        """
          Expected and actual maps do not match. Differences are:

          field with path '$.key' differ:
          - actual value  :
          - expected value:
        """
      );
  }

  @Test
  @DisplayName("MapAssertions#assertMapsMatch throws error when actual property is Integer and expected property is Float")
  void g() {
    // Arranje
    Map<String, Object> actual = new HashMap();
    Map<String, Object> innerActual = new HashMap();
    actual.put("key", innerActual);

    Map<String, Object> expected = new HashMap();
    Map<String, Object> innerExpected = new HashMap();
    innerExpected.put("innerKey", "5");
    expected.put("key",innerExpected);

    // Act
    // Assert
    Assertions.assertThatThrownBy(() -> new MapAssertions().assertThat(actual).assertMapsMatch(expected))
      .isInstanceOf(AssertionError.class)
      .hasMessage(
        """
          Expected and actual maps do not match. Differences are:

          field with path '$.key' differ:
          - actual value  :
          - expected value:
          actual and expected have different sizes, actual size is [0] and expected size is [1]
        """
      );
  }

  @Test
  @DisplayName("MapAssertions#assertMapsMatch throws error when actual property is String and expected property is Integer")
  void h() {
    // Arranje
    Map<String, Object> actual = new HashMap();
    actual.put("key", "5");

    Map<String, Object> expected = new HashMap();
    expected.put("key",5);

    // Act
    // Assert
    Assertions.assertThatThrownBy(() -> new MapAssertions().assertThat(actual).assertMapsMatch(expected))
      .isInstanceOf(AssertionError.class)
      .hasMessage(
        """
          Expected and actual maps do not match. Differences are:

          field with path '$.key' differ:
          - actual value  : "5"
          - expected value: 5
        """
      );
  }*/
}

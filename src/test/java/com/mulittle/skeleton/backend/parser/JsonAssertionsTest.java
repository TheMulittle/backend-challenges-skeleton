package com.mulittle.skeleton.backend.parser;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JsonAssertionsTest {
  @Test
  @DisplayName("JsonAssertions#assertJsonsMatch does not throw error when jsons match")
  void testAssertJsonsMatch() {
    // Arranje
    String actual = """
      {
        "id": 5
      }
    """;

    String expected = """
      {
        "id": 5
      }
    """;

    // Act
    JsonAssertions.assertJsonsMatch(actual, expected);
  }

  @Test
  @DisplayName("JsonAssertions#assertJsonsMatch throws assertion error when primitive properties do not match")
  void testPrimitiveMatches() {
    // Arranje
    String actual = """
      {
        "id": 4
      }
    """;

    String expected = """
      {
        "id": 5
      }
    """;

    // Act
    // Assert
    Assertions.assertThatThrownBy(() -> JsonAssertions.assertJsonsMatch(actual, expected))
      .isInstanceOf(AssertionError.class)
      .hasMessage(expected);
  }

  @Test
  @DisplayName("JsonAssertions#assertJsonsMatch throws assertion error when an actual map key does not exist in expected map")
  void c() {
    // Arranje
    String actual = """
      {
        "id": 4
      }
    """;

    String expected = """
      {
        "idx": 4
      }
    """;

    // Act
    // Assert
    Assertions.assertThatThrownBy(() -> JsonAssertions.assertJsonsMatch(actual, expected))
      .isInstanceOf(AssertionError.class)
      .hasMessage(expected);
  }
}

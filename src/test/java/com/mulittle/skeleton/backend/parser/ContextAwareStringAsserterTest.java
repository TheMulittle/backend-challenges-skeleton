package com.mulittle.skeleton.backend.parser;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;

import com.mulittle.skeleton.backend.context.Context;

public class ContextAwareStringAsserterTest {

  @Mock
  Context contextMock;

  @DisplayName("ContextAwareStringAsserter#assertString does not return assertion difference when actual matches Integer placeholder matcher")
  @ParameterizedTest
  @ValueSource(ints = { 0, 2 })
  public void does_not_return_assertion_difference_when_actual_is_integer_between_boundaries_and_expected_is_integer_placeholder_matcher(
      Integer actual) {
    // Arranje
    ContextAwareStringAsserter stringAsserter = new ContextAwareStringAsserter(contextMock);

    String expected = "P@|Integer_0_2|";

    // Act
    Optional<AssertionDifference> assertionDifference = stringAsserter.assertString(actual, expected);

    // Assert
    Assertions.assertThat(assertionDifference).isNotPresent();

  }

  @Test
  @DisplayName("ContextAwareStringAsserter#assertString returns assertion difference when actual is Integer and does not match Integer placeholder matcher")
  public void returns_assertion_difference_when_actual_does_not_match_integer_placeholder_matcher_boundaries() {
    // Arranje
    ContextAwareStringAsserter stringAsserter = new ContextAwareStringAsserter(contextMock);

    Integer actual = 3;

    String expected = "P@|Integer_0_2|";

    String message = "Actual should be an integer between [0] and [2] (inclusive)";
    AssertionDifference expectedAssertionDifference = new AssertionDifference(3, "P@|Integer_0_2|", message);

    // Act
    Optional<AssertionDifference> actualAssertionDifference = stringAsserter.assertString(actual, expected);

    // Assert
    Assertions.assertThat(actualAssertionDifference).isPresent();
    Assertions.assertThat(actualAssertionDifference.get())
        .usingRecursiveComparison()
        .isEqualTo(expectedAssertionDifference);
  }

  @Test
  @DisplayName("ContextAwareStringAsserter#assertString returns assertion difference when actual is not an integer and expected is an Integer placeholder macther")
  public void returns_assertion_difference_when_actual_is_not_integer_and_expected_is_an_integer_placeholder_matcher() {
    // Arranje
    ContextAwareStringAsserter stringAsserter = new ContextAwareStringAsserter(contextMock);

    String actual = "string";

    String expected = "P@|Integer_0_2|";

    String expectedMessage = """
        field with path '' differ:
        - actual value  : "string"
        - expected value: "P@|Integer_0_2|"
        Actual should be an [Integer], but it is a [class java.lang.String]
        """;

    // Act
    Optional<AssertionDifference> actualAssertionDifference = stringAsserter.assertString(actual, expected);

    // Assert
    Assertions.assertThat(actualAssertionDifference).isPresent();
    Assertions.assertThat(actualAssertionDifference.get().getMessage())
        .isEqualTo(expectedMessage);

  }

  @ParameterizedTest
  @ValueSource(strings = { "P@|Integer_0|", "P@|Integer_0_2_3|" })
  @DisplayName("ContextAwareStringAsserter#assertString throws exception when expected Integer placeholder matcher has number of arguments different than two")
  public void throws_exception_when_expcted_integer_placeholder_matcher_does_not_have_correct_number_of_arguments(
      String expected) {
    // Arranje
    ContextAwareStringAsserter stringAsserter = new ContextAwareStringAsserter(contextMock);

    Object actual = new Object();
    // Act
    Assertions.assertThatThrownBy(() -> stringAsserter.assertString(actual, expected))
        .isInstanceOf(IllegalArgumentException.class);

  }
}

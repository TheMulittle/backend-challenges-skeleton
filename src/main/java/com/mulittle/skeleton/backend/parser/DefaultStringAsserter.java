package com.mulittle.skeleton.backend.parser;

import java.util.Optional;

public class DefaultStringAsserter implements StringAsserter {
  public Optional<AssertionDifference> assertString(Object actualValue, String expectedValue) {
    Optional<AssertionDifference> assertionDifference = Optional.empty();

    if(!expectedValue.equals(actualValue)) {
      assertionDifference = Optional.of(new AssertionDifference(actualValue, expectedValue));
    }

    return assertionDifference;
  }
}

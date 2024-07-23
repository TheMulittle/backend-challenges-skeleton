package com.mulittle.skeleton.backend.parser;

import java.util.Optional;

public interface StringAsserter extends Asserter {
  Optional<AssertionDifference> assertString(Object actualValue, String expectedValue);
}

package com.mulittle.skeleton.backend.parser;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mulittle.skeleton.backend.context.Context;

public class ContextAwareStringAsserter extends DefaultStringAsserter {

  public static final String PLACHOLDER_MATCHER_PATTERN = "P@\\|(.*?)(_(.*)?)*\\|";

  public static final String TYPE_MISMATCH_REASON = "Actual should be an [%s], but it is a [%s]";

  public static final String INTEGER_OUT_OF_BOUNDS = "Actual should be an integer between [%d] and [%d] (inclusive)";

  public Context context;

  public ContextAwareStringAsserter(Context context) {
    this.context = context;
  }

  public Optional<AssertionDifference> assertString(Object actualValue, String expectedValue) {

    Matcher matcher = Pattern.compile(PLACHOLDER_MATCHER_PATTERN).matcher(expectedValue);

    if(matcher.find()) {
      return assertPlaceholder(actualValue, expectedValue, matcher.group(1), matcher.group(3).split("_"));
    }

    return super.assertString(actualValue, expectedValue);
  }

  private Optional<AssertionDifference> assertPlaceholder(Object actualValue, String expectedValue, String operation, String[] arguments) {
    switch (operation) {
      case "Long":
        if(arguments.length != 2) {
          throw new IllegalArgumentException();
        }
        return matchesInteger(actualValue, expectedValue, arguments[0], arguments[1]);
    
      default:
        return Optional.empty();
    }

  }

  private Optional<AssertionDifference> matchesInteger(Object actualValue, String expectedValue, String argument1, String argument2) {
    
    if(!(actualValue instanceof Integer)) {
      String message = TYPE_MISMATCH_REASON.formatted("Integer", actualValue.getClass());
      return Optional.of(new AssertionDifference(actualValue, expectedValue, message));
    }

    Integer actualAsInteger = (Integer) actualValue;
    Integer lowerBounduary = Integer.valueOf(argument1);
    Integer upperBounduary = Integer.valueOf(argument2);
    
    if(actualAsInteger < lowerBounduary || actualAsInteger > upperBounduary) {
      String message = INTEGER_OUT_OF_BOUNDS.formatted(lowerBounduary, upperBounduary);
      return Optional.of(new AssertionDifference(actualAsInteger, expectedValue, message.formatted(lowerBounduary, upperBounduary)));
    }

    return Optional.empty();
  }
}

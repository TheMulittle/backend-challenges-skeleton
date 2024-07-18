package com.mulittle.skeleton.backend.parser;

public class AssertionDifference {

  String DIFFERENCE_MESSAGE = 

  """
  field with path '%s' differ:
  - actual value  : %s
  - expected value: %s  
  %s
  """;

  public AssertionDifference(String path, Object actual, Object expected, String reason) {
    this.path = path;
    this.actual = actual;
    this.expected = expected;
    this.reason = reason;
  }

  public AssertionDifference(String path, Object actual, Object expected) {
    this.path = path;
    this.actual = actual;
    this.expected = expected;
    this.reason = "";
  }

  public String path;
  public Object actual;
  public Object expected;
  public String reason;

  public Object getMessage() {
    return DIFFERENCE_MESSAGE.formatted(path, actual.toString(), expected.toString(), reason);
  }
}
